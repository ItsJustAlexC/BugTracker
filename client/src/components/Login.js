import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import jwtDecode from "jwt-decode";

function Login({ setUser, SERVER_URL }) {
  const navigate = useNavigate();
  const [loginData, setLoginData] = useState({ username: "", password: "" });
  const [err, setErr] = useState("");

  const handleSubmit = function (evt) {
    evt.preventDefault();

    fetch(SERVER_URL + "/api/authenticate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginData),
    })
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else {
          setErr("Username and/or password is incorrect");
        }
      })
      .then((jwtContainer) => {
        if (!jwtContainer) {
          return;
        }

        const jwt = jwtContainer.jwt_token;
        let userData = jwtDecode(jwt);
        userData.authorities = userData.authorities.split(",");

        const newUser = {
          token: jwt,
          userData,
        };

        localStorage.setItem("userData", JSON.stringify(newUser));

        setUser(newUser);

        navigate("/");
      });
  };

  const handleChange = function (evt) {
    let newLoginData = { ...loginData };
    newLoginData[evt.target.id] = evt.target.value;
    setLoginData(newLoginData);
  };

  return (
    <div className="container d-flex flex-column justify-content-center align-items-center h-100 vw-100 mt-5">
      <div className="bg-light rounded shadow p-3 mb-5 bg-white rounded">
        <div className="text-center mb-3 mt-4"> <h3>Login</h3> </div>
        <form onSubmit={handleSubmit} className="form m-3">
          <div>
            <label className="form-label" htmlFor="username"> Username </label>
            <input type="text" className="form-control mb-2" id="username" onChange={handleChange} value={loginData.username}/>
          </div>

          <div>
            <label className="form-label" htmlFor="password">Password</label>
            <input type="password" className="form-control mb-2" id="password" onChange={handleChange} value={loginData.password}/>
          </div>

          <div className="justify-content-center text-wrap">
            <p className="text-danger font-italic mt-3 text-center" style={{ maxWidth: "200px", wordBreak: "break-word" }}>{err}</p>
          </div>
          <div className="d-flex  justify-content-center">
            <button id="login" type="submit" className="btn btn-primary ml-auto mr-auto"> Login </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Login;
