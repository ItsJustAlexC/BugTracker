import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function CreateAccount({SERVER_URL}) {
    const [loginData, setLoginData] = useState({username:"", password:"", passwordConfirm:""})
    const [valid, setValid] = useState(false);
    const [err, setErr] = useState("");
    const navigate = useNavigate();

    const handleSubmit = function (evt) {
        evt.preventDefault();
        
        if(!valid) {
            return;
        }

        const submission = {username: loginData.username, password: loginData.password};

        fetch(SERVER_URL + "/api/create_account", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
              },
            body: JSON.stringify(submission)
        })
        .then((response) => {
            if(response.ok) {
                console.log(response);
                navigate("/login");
            }
            else {
                console.log(response);
                setErr("Failed to create account.");
            }
        });
    }

    const handleChange = function (evt) {
        let newLoginData = {...loginData};
        newLoginData[evt.target.id] = evt.target.value;

        if(newLoginData.password === newLoginData.passwordConfirm) {
            setValid(true);
            setErr("");
        }
        else {
            setValid(false);
            setErr("Passwords must match.");
        }
        setLoginData(newLoginData);
    }

    return (
        <div className="container d-flex flex-column justify-content-center align-items-center h-100 vw-100 mt-5">
            <div className="bg-light rounded shadow p-3 mb-5 bg-white rounded">
                <div className="text-center"> <h3 className="m-3">Create Account</h3> </div>
                <form onSubmit={handleSubmit} className="form m-3">
                    <div>
                        <label className="form-label" htmlFor="username">Username</label>
                        <input type="text" className="form-control" id="username" onChange={handleChange} value={loginData.username}/>
                    </div>
                    <div>
                        <label className="form-label" htmlFor="password">Password</label>
                        <input type="password" className="form-control" id="password" onChange={handleChange} value={loginData.password}/>
                    </div>
                    <div>
                        <label className="form-label" htmlFor="passwordConfirm">Confirm Password</label>
                        <input type="password" className="form-control" id="passwordConfirm" onChange={handleChange} value={loginData.passwordConfirm}/>
                    </div>
                    <div className="d-flex flex-column justify-content-center">
                        <p className="text-danger text-sm-left font-italic text-center mt-2">{err}</p>
                        <button id="submit" type="submit" className="btn btn-primary">Submit</button>
                    </div>     
                </form>
            </div>
        </div>
    );
}

export default CreateAccount;