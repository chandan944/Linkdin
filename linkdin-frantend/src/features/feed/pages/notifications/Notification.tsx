import {  useEffect, useState, type Dispatch, type SetStateAction } from "react";
import { useNavigate } from "react-router-dom";
import { request } from "../../../../utils/api";
import classes from "./Notification.module.scss";
import { RightSidebar } from "../../components/rightSideBar/RightSideBar";
import type { User } from "../../../authentication/context/AuthenticationContextProvider";
import { LeftSideBar } from "../../components/leftSideBar/LeftSideBar";
import { TimeAgo } from "../../timeAgo/TimeAgo";



enum NotificationType {
  LIKE = "LIKE",
  COMMENT = "COMMENT",
}
export interface Notification {
  id: number;
  recipient: User;
  actor: User;
  read: boolean;
  type: NotificationType;
  resourceId: number;
  creationDate: string;
}

export function Notifications() {
  const [notifications, setNotifications] = useState<Notification[]>([]);

  useEffect(() => {
    const fetchNotifications = async () => {
      await request<Notification[]>({
        endpoint: "/api/v1/notifications",
        onSuccess: setNotifications,
        onFailure: (error) => console.log(error),
      });
    };

    fetchNotifications();
  }, []);

  return (
    <div className={classes.root}>
      <div className={classes.left}>
        <LeftSideBar />
      </div>
      <div className={classes.center}>
        {notifications.map((notification) => (
          <Notification
            key={notification.id}
            notification={notification}
            setNotifications={setNotifications}
          />
        ))}
        {notifications.length === 0 && (
          <p
            style={{
              padding: "1rem",
            }}
          >
            No notifications
          </p>
        )}
      </div>
      <div className={classes.right}>
        <RightSidebar />
      </div>
    </div>
  );
}

function Notification({
  notification,
  setNotifications,
}: {
  notification: Notification;

  setNotifications: Dispatch<SetStateAction<Notification[]>>;
}) {
  const navigate = useNavigate();
  
  function markNotificationAsRead(notificationId: number) {
    request({
      endpoint: `/api/v1/notifications/${notificationId}`,
      method: "PUT",
      onSuccess: () => {
        setNotifications((prev) =>
          prev.map((notification) =>
            notification.id === notificationId ? { ...notification, isRead: true } : notification
          )
        );
      },
      onFailure: (error) => console.log(error),
    });
  }
  return (
    <button
      onClick={() => {
        markNotificationAsRead(notification.id);
        navigate(`/posts/${notification.resourceId}`);
      }}
      className={
        notification.read ? classes.notification : `${classes.notification} ${classes.unread}`
      }
    >
      <img src={notification.actor.profilePicture} alt="" className={classes.avatar} />

      <p
        style={{
          marginRight: "auto",
        }}
      >
        <strong>{notification.actor.firstName + " " + notification.actor.lastName}</strong>{" "}
        {notification.type === NotificationType.LIKE ? "liked" : "commented on"} your post.
      </p>
      <TimeAgo date={notification.creationDate} />
    </button>
  );
}
