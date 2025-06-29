import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import classes from "./Feed.module.scss";
import { usePageTitle } from "../../../../hook/usePageTitle";
import { useAuthentication } from "../../../authentication/context/AuthenticationContextProvider";
import { Post } from "../../components/post/Post";
import { LeftSideBar } from "../../components/leftSideBar/LeftSideBar";
import { Button } from "../../../../components/button/Button";
import { Madal } from "../../components/modal/Model";
import { RightSidebar } from "../../components/rightSideBar/RightSideBar";

export function Feed() {
  usePageTitle("Feed");
  const [showPostingModal, setShowPostingModal] = useState(false);
  const [feedContent, setFeedContent] = useState<"all" | "connexions">("connexions");
  const { user } = useAuthentication();
  const navigate = useNavigate();
  const [posts, setPosts] = useState<Post[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const response = await fetch(
          import.meta.env.VITE_API_URL +
            "/api/v1/feed" +
            (feedContent === "connexions" ? "" : "/posts"),

          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
          }
        );
        if (!response.ok) {
          const { message } = await response.json();
          throw new Error(message);
        }
        const data = await response.json();
        setPosts(data);
      } catch (error) {
        if (error instanceof Error) {
          setError(error.message);
        } else {
          setError("An error occurred. Please try again later.");
        }
      }
    };
    fetchPosts();
  }, [feedContent]);

  const handlePost = async (content: string, picture: string) => {
    const response = await fetch(import.meta.env.VITE_API_URL + "/api/v1/feed/posts", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify({ content, picture }),
    });
    if (!response.ok) {
      const { message } = await response.json();
      throw new Error(message);
    }
    const data = await response.json();
    setPosts([data, ...posts]);
  };

  return (
    <div className={classes.root}>
      <div className={classes.left}>
        <LeftSideBar />
      </div>
      <div className={classes.center}>
        <div className={classes.posting}>
          <button
            onClick={() => {
              navigate(`/profile/${user?.id}`);
            }}
          >
            <img
              className={`${classes.top} ${classes.avatar}`}
              src={user?.profilePicture || "/avatar.png"}
              alt=""
            />
          </button>
          <Button outline onClick={() => setShowPostingModal(true)}>
            Start a post
          </Button>
          <Madal
            title="Creating a post"
            onSubmit={handlePost}
            showModal={showPostingModal}
            setShowModal={setShowPostingModal}
          />
        </div>
        {error && <div className={classes.error}>{error}</div>}

        <div className={classes.header}>
          <button
            className={feedContent === "all" ? classes.active : ""}
            onClick={() => setFeedContent("all")}
          >
            All
          </button>
          <button
            className={feedContent === "connexions" ? classes.active : ""}
            onClick={() => setFeedContent("connexions")}
          >
            Feed
          </button>
        </div>

        <div className={classes.feed}>
          {posts.map((post) => (
            <Post key={post.id} post={post} setPosts={setPosts} />
          ))}
        </div>
      </div>
      <div className={classes.right}>
        <RightSidebar />
      </div>
    </div>
  );
}
