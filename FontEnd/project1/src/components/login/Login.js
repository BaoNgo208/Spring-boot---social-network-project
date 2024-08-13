import React, { useState, useEffect } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import { useNavigate } from 'react-router-dom';
import { LoginPage } from "../pages/LoginPage.js";
import { connectWebSocket,registerMessageCallback } from "../../helpers/WebSocketService.js";

const Login = () => {
    const navigate = useNavigate();
    const [state, setState] = useState({
        username: "",
        password: ""
    });
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    const setParams = (event) => {
        setState({
            ...state,
            [event.target.name]: event.target.value
        });
    };

    useEffect(() => {
        if (isLoggedIn) {

            navigate('/home');
        }
    }, [isLoggedIn]);

    const login = () => {
        const myHeaders = new Headers();
        const credentials = state.username + ':' + state.password;
        const encodedCredentials = btoa(credentials);
        myHeaders.append("Authorization", "Basic " + encodedCredentials);

        const requestOptions = {
            method: "POST",
            headers: myHeaders,
        };
        const onMessageReceived = (message) => {
            console.log("Received message:", message);
            // Handle received message here
          };

        fetch("http://localhost:8080/auth/sign-in", requestOptions)
            .then((response) => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Wrong email or password");
                }
            })
            .then((result) => {
                // Cookies.set("accessToken", result.access_token, {expires : 14});
                // Cookies.set("email", state.username,{expires : 14});
                // Cookies.set("cookie", result.refresh_token,{expires : 14});

                sessionStorage.setItem("accessToken", result.access_token);
                sessionStorage.setItem("email", state.username);
                sessionStorage.setItem("cookie", result.refresh_token);
                sessionStorage.setItem("username", result.user_name);
                sessionStorage.setItem("userId", result.user_Id);
                navigate('/home');
                connectWebSocket( onMessageReceived);

            })
            .catch((error) => console.error("Login error:", error));

    };

    return (
        <LoginPage setParams={setParams} login={login}/>
    );


};

export default Login;
