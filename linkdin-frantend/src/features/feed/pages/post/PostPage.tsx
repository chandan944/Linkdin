import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { request } from "../../../../utils/api";
;
import classes from "./PostPage.module.scss";
import { LeftSideBar } from "../../components/leftSideBar/LeftSideBar";
import { RightSidebar } from "../../components/rightSideBar/RightSideBar";
import { Post } from "../../components/post/Post";
export function PostPage() {
  const [posts, setPosts] = useState<Post[]>([]);
  const { id } = useParams();

  useEffect(() => {
    request<Post>({
      endpoint: `/api/v1/feed/posts/${id}`,
      onSuccess: (post) => setPosts([post]),
      onFailure: (error) => console.log(error),
    });
  }, [id]);

  return (
    <div className={classes.root}>
      <div className={classes.left}>
        <LeftSideBar />
      </div>
      <div className={classes.center}>
        {posts.length > 0 && <Post setPosts={setPosts} post={posts[0]} />}
      </div>
      <div className={classes.right}>
        <RightSidebar />
      </div>
    </div>
  );
}
