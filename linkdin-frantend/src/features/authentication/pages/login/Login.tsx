import { FormEvent, useState } from "react"; // <-- FIXED

import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuthentication } from "../../context/AuthenticationContextProvider";
import Layout from "../../components/layout/Layout";
import Box from "../../components/box/Box";
import { Input } from "../../../../components/input/Input";
import { Button } from "../../../../components/button/Button";
import { Seperator } from "../../../../components/seprator/Seperator";
import classes from "./Login.module.scss";


export function Login() {
  const [errorMessage, setErrorMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { login } = useAuthentication();
  const doLogin = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);
    const email = e.currentTarget.email.value;
    const password = e.currentTarget.password.value;

    try {
      await login(email, password);
      const destination = location.state?.from || "/";
      navigate(destination);
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage("An unknown error occurred.");
      }
    } finally {
      setIsLoading(false);
    }
  };
  return (
    <Layout >
      <Box>
        <div className={classes.root}>
        <h1>Sign in</h1>
        <p>Stay updated on your professional world.</p>
        <form onSubmit={doLogin}>
          <Input label="Email" type="email" id="email" onFocus={() => setErrorMessage("")} />
          <Input
            label="Password"
            type="password"
            id="password"
            onFocus={() => setErrorMessage("")}
          />
          {errorMessage && <p className={classes.error}>{errorMessage}</p>}

          <Button type="submit" disabled={isLoading}>
            {isLoading ? "..." : "Sign in"}
          </Button>
          <Link to="/request-password-reset">Forgot password?</Link>
        </form>
        <Seperator>Or</Seperator>
        <div className={classes.register}>
          New to LinkedIn? <Link to="/signup">Join now</Link>
        </div></div>
      </Box>
    </Layout>
  );
}
