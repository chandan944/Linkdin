const BASE_URL = import.meta.env.VITE_API_URL;

interface RequestParams<T> {
  endpoint: string;
  method?: "GET" | "POST" | "PUT" | "DELETE";
  body?: BodyInit;
  onSuccess: (data: T) => void;
  onFailure: (error: string) => void;
}

export const request = async <T>({
  endpoint,
  method = "GET",
  body,
  onSuccess,
  onFailure,
}: RequestParams<T>): Promise<void> => {
  try {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        "Content-Type": "application/json",
      },
      body,
    });

    if (!response.ok) {
      const { message } = await response.json();
     return onFailure(message);
    }
    const data: T = await response.json();

    onSuccess(data);
  } catch (error) {
      const fallbackMessage = error instanceof Error ? error.message : "Something went wrong!";
    onFailure(fallbackMessage); 
  }
};
