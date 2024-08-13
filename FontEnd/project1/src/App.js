import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate , useLocation} from 'react-router-dom';
import Login from './components/login/Login';
import { Home } from './components/pages/Home/homepage/HomePage';
import { ProtectedRoutes } from './ProtectedRoutes.tsx';
import { SignUpPage } from './components/pages/SignUpPage.js';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AuthCheck from './helpers/AuthCheck';
import {Profile} from './components/pages/ProfilePage/Profile';
import { connectWebSocket ,disconnectWebSocket} from './helpers/WebSocketService';
import './App.css';
const App = () => {
    useEffect(() => {
        const token = sessionStorage.getItem("accessToken");
        if (token) {
          connectWebSocket();
        }
    
        return () => {
          disconnectWebSocket();
        };
      }, []);
    const queryClient = new QueryClient();

    return (
        <QueryClientProvider client={queryClient}>
            <Router>
                <AuthWrapper />
                <Routes>
                    <Route path="/" element={<Login />} />
                    <Route path="/signup" element={<SignUpPage />} />
                    <Route element={<ProtectedRoutes />}>
                        <Route path="/home" element={<Home />} />
                        <Route path="/profile" element={<Profile />} /> 
                    </Route>
                </Routes>
            </Router>
        </QueryClientProvider>
    );
};

const AuthWrapper = () => {
    const location = useLocation();
    return <AuthCheck location={location} />;
};

export default App;
