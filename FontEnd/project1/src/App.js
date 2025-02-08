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
import { SearchedUserPage } from './components/pages/SearchedUserPage/SearchedUserPage';
import { RecommendUsersProvider } from './helpers/context/RecommendUsersContext';
import { FriendProvider } from './helpers/context/FriendContext';
import { ChatProvider } from './helpers/context/ChatContext';
import { SelectedFriendMessagesProvider } from './helpers/context/SelectedFriendMessagesContext';
import Chat from './components/pages/Home/Chat/Chat';
import { useChat } from './helpers/context/ChatContext';

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
                <RecommendUsersProvider>
                <FriendProvider>
                <PostProvider>
                <ChatProvider>
                <SelectedFriendMessagesProvider>

                    <Router>
                        <AuthWrapper />
                        <Routes>
                            <Route path="/" element={<Login />} />
                            <Route path="/signup" element={<SignUpPage />} />
                            <Route element={<ProtectedRoutes />}>
                                <Route path="/home" element={<Layout><Home /></Layout>} />
                                <Route path="/profile" element={<Layout><ProfileWrapper /></Layout>} /> 
                                <Route path="/postDetail/:id" element={<Layout><PostDetailPage /></Layout> } />
                                <Route path='/search-results' element = {<Layout><SearchedUserPage/></Layout> } />
                            </Route>
                        </Routes>
                    </Router>
                
                </SelectedFriendMessagesProvider>
                </ChatProvider>    
                </PostProvider>
                </FriendProvider>
                
                </RecommendUsersProvider>
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
