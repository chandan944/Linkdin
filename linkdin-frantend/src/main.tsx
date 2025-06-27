import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.scss'
import { createBrowserRouter, Navigate, RouterProvider } from 'react-router-dom'
import { AuthenticationContextProvider } from './features/authentication/context/AuthenticationContextProvider';
import { Feed } from './features/feed/pages/feed/Feed';
import { Login } from './features/authentication/pages/login/Login';
import { Signup } from './features/authentication/pages/signup/Signup';
import { ResetPassword } from './features/authentication/pages/ResetPassword/ResetPassword';
import { VerifyEmail } from './features/authentication/pages/verifyEmail/VerifyEmail';
import { ApplicationLayout } from './components/applicationLayout/ApplicationLayout';
import { AuthenticationLayout } from './features/authentication/components/layout/AuthenticationLayout';
import { Profile } from './features/authentication/pages/Profile/Profile';
import { PostPage } from './features/feed/pages/post/PostPage';
import { Notifications } from './features/feed/pages/notifications/Notification';






const router = createBrowserRouter([
  {
    element: <AuthenticationContextProvider />, // 🌐 Wraps all the routes with login info
    children: [
      {
        path: "/", // 🌍 Root path = Homepage
        element: <ApplicationLayout/>, // 🧱 This is like the base layout for logged-in users (navbar, sidebar, etc.)
        children: [
          {
            index: true, // 🏠 If someone visits just `/`, show the Feed page!
            element: <Feed />,
          },
            {
            path: "posts/:id",
            element: <PostPage />,
          },
          {
            path: "network", // 👥 LinkedIn's Network tab
            element: <div>Network</div>,
          },
          {
            path: "jobs", // 💼 Jobs tab
            element: <div>Jobs</div>,
          },
          {
            path: "messaging", // 💬 Messaging tab
            element: <div>Messaging</div>,
          },
          {
            path: "notifications", // 🔔 Notification tab
            element: <Notifications/>,
          },
          {
            path: "profile/:id", // 👤 Dynamic profile pages like `/profile/123`
            element: <div>Profile</div>,
          },
          {
            path: "settings", // ⚙️ Settings page
            element: <div>Settings & Privacy</div>,
          },
        ],
      },

      // 🔐 Authentication Routes
      {
        path: "/authentication", // All auth-related routes start here!
        element: <AuthenticationLayout/>, // 📦 A layout for login/signup/etc.
        children: [
          {
            path: "login",
            element: <Login />, // 👤 Login form
          },
          {
            path: "signup",
            element: <Signup />, // ✍️ Signup form
          },
          {
            path: "request-password-reset",
            element: <ResetPassword />, // 🔑 Forgot password
          },
          {
            path: "verify-email",
            element: <VerifyEmail />, // ✅ Verify email code
          },
          {
            path: "profile/:id",
            element: <Profile />, // 🧑‍💼 Your profile (after login maybe)
          },
        ],
      },

      // 🧨 If someone types a random URL — go home!
      {
        path: "*",
        element: <Navigate to="/" />, // 📦 Redirect all unknown paths to homepage
      },
    ],
  },
]);



createRoot(document.getElementById('root')!).render(
  <StrictMode >
    <RouterProvider router={router}/>
    
  </StrictMode>,
)
