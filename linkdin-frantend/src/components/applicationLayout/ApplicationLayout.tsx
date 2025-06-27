import { Outlet } from "react-router-dom";

import classes from "./ApplicationLayout.module.scss";
import { Header } from "../header/Header";
import { WebSocketContextProvider } from "../../features/ws/WebSocketContextProvider";

export function ApplicationLayout() {
  return (
    <WebSocketContextProvider>
      <div className={classes.root}>
        <Header />
        <main className={classes.container}>
          <Outlet />
        </main>
      </div>
    </WebSocketContextProvider>
  );
}
