import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import Login from './components/login/Login';
import { Home } from './components/pages/Home/homepage/HomePage';
import { ProtectedRoutes } from './ProtectedRoutes.tsx';
import { SignUpPage } from './components/pages/SignUpPage.js';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AuthCheck from './helpers/AuthCheck';
import { Profile } from './components/pages/ProfilePage/Profile';
import { connectWebSocket, disconnectWebSocket } from './helpers/WebSocketService';
import PostDetailPage from './components/pages/Home/post/PostDetail/PostDetailPage';
import './App.css';
import { PostProvider } from './components/pages/Home/post/PostDetail/PostContext';
import { NotificationProvider } from './components/pages/Home/navbar/Notification/NotificationContext';
import Layout from './components/pages/Home/navbar/Layout';

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
            <NotificationProvider>
                <PostProvider>
                    <Router>
                        <AuthWrapper />
                        <Routes>
                            <Route path="/" element={<Login />} />
                            <Route path="/signup" element={<SignUpPage />} />
                            <Route element={<ProtectedRoutes />}>
                                <Route path="/home" element={<Layout><Home /></Layout>} />
                                <Route path="/profile" element={<Layout><ProfileWrapper /></Layout>} /> {/* Sử dụng ProfileWrapper */}
                                <Route path="/postDetail/:id" element={<PostDetailPage />} />
                            </Route>
                        </Routes>
                    </Router>
                </PostProvider>
            </NotificationProvider>
        </QueryClientProvider>
    );
};

const AuthWrapper = () => {
    const location = useLocation();
    return <AuthCheck location={location} />;
};

// ProfileWrapper component để bao bọc Profile và thêm key prop
const ProfileWrapper = () => {
    const location = useLocation();
    return <Profile key={location.search} />;
};

export default App;
