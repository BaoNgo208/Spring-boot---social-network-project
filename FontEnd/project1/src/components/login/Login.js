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

    const login = async () => {
        try {
            const myHeaders = new Headers();
            const credentials = state.username + ':' + state.password;
            const encodedCredentials = btoa(credentials);
            myHeaders.append("Authorization", "Basic " + encodedCredentials);
    
            const requestOptions = {
                method: "POST",
                headers: myHeaders,
            };
    
            const response = await fetch("http://localhost:8080/auth/sign-in", requestOptions);
    
            if (!response.ok) {
                throw new Error("Wrong email or password");
            }
    
            const result = await response.json();
    
            sessionStorage.setItem("accessToken", result.access_token);
            sessionStorage.setItem("email", state.username);
            sessionStorage.setItem("cookie", result.refresh_token);
            sessionStorage.setItem("username", result.user_name);
            sessionStorage.setItem("userId", result.user_Id);
            sessionStorage.setItem("accName", result.acc_name);
            sessionStorage.setItem("userInfo", JSON.stringify(result.user_info));
    
            navigate('/home');
    
            const onMessageReceived = (message) => {
                console.log("Received message:", message);
            };
            connectWebSocket(onMessageReceived);
        } catch (error) {
            console.error("Login error:", error.message);
            alert("Login error:"+ error.message);
        }
    };
    

    return (
        <LoginPage setParams={setParams} login={login}/>
    );


};

export default Login;