// 🔽 These are React and React Router tools
import { useState, type FormEvent } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

// 🎨 This imports CSS module styles (used like classes.error, classes.root, etc.)
import classes from "./Login.module.scss";

// 🔐 This is your custom hook to access login logic from AuthContext
import { useAuthentication } from "../../context/AuthenticationContextProvider";

// ✅ These are some custom reusable UI components
import Box from "../../components/box/Box";

import { Seperator } from "../../../../components/seprator/Seperator";
import { Button } from "../../../../components/button/Button";

// 🖼️ Hook to set the page title in the browser tab
import { usePageTitle } from "../../../../hook/usePageTitle";
import { Input } from "../../../../components/input/Input";


// 🚪 MAIN FUNCTION: This shows the Login form and handles login logic
export function Login() {
  // 🧠 State to hold error messages (like: wrong password)
  const [errorMessage, setErrorMessage] = useState("");

  // 🌀 This shows a loading spinner or disables button during login
  const [isLoading, setIsLoading] = useState(false);

  // 🧭 Tells us from which page the user came before redirecting to login
  const location = useLocation();

  // 🚀 Lets us redirect the user after successful login
  const navigate = useNavigate();

  // 🎨 Sets page title on tab
  usePageTitle("Login");

  // 🔐 Gets login function from AuthContext
  const { login } = useAuthentication();

  // 🔁 Called when user submits the form
 const doLogin = async (e: FormEvent<HTMLFormElement>) => {
  e.preventDefault();
  setIsLoading(true);

  const email = e.currentTarget.email.value;
  const password = e.currentTarget.password.value;

  try {
    await login(email, password);
    const destination = location.state?.from || "/";
    navigate(destination);
  } catch (e) {
    // 🧠 Catch error with meaningful message
    if (e instanceof Error) {
      setErrorMessage(e.message); // e.g., "Password is incorrect."
    } else {
      setErrorMessage("Something went wrong... 😵");
    }
  } finally {
    setIsLoading(false);
  }
};


  // 📦 Render UI
  return (
    <div className={classes.root}>
      <Box>
        <h1>Sign in</h1>
        <p>Stay updated on your professional world.</p>

        {/* 🧾 FORM START */}
        <form onSubmit={doLogin}>
          {/* 🧑 Email input */}
          <Input
            label="Email"
            type="email"
            id="email"
            onFocus={() => setErrorMessage("")} // 🔄 Clear error on typing
          />

          {/* 🔐 Password input */}
          <Input
            label="Password"
            type="password"
            id="password"
            onFocus={() => setErrorMessage("")}
          />

          {/* 🚨 ERROR MESSAGE */}
          {errorMessage && (
            <p className={classes.error}>{errorMessage}</p>
          )}

          {/* 🧲 SUBMIT BUTTON */}
          <Button type="submit" disabled={isLoading}>
            {isLoading ? "Logging in..." : "Sign in"}
          </Button>

          {/* ❓ Forgot password */}
          <Link to="/authentication/request-password-reset">
            Forgot password?
          </Link>
        </form>

        {/* ➖ OR SEPARATOR */}
        <Seperator>Or</Seperator>

        {/* 🆕 Sign up link */}
        <div className={classes.register}>
          New to LinkedIn?{" "}
          <Link to="/authentication/signup">Join now</Link>
        </div>
      </Box>
    </div>
  );
}
