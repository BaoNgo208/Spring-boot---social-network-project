import React, { useEffect, useState } from 'react';
import { useQuery } from "@tanstack/react-query";
import CoverPhoto from './CoverPhoto';
import ProfilePicture from './ProfilePicture';
import UserInfo from './UserInfo';
import PostList from './PostList';
import { userData } from './data';
import api from '../../../helpers/api';
import profileUserImg from '../../../assests/woman.jpg';
import { useLocation } from 'react-router-dom';
import './Profile.css';

export const Profile = () => {
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);


    const { data: userPost, isLoading: isLoadingInfo, isError: isErrorInfo } = useQuery({
        queryKey: "userPost",
        queryFn: async () => {
            if (queryParams.get('emailId') === null) {
                const response = await api.get("http://localhost:8080/employee/get/profile");
                return response.data;
            } else {
                const response = await api.get("http://localhost:8080/employee/get/profile?emailId=" + queryParams.get("emailId"));
                return response.data;
            }
        }
    });

    const { data: friendList = [] } = useQuery({
        queryKey: "friendList",
        queryFn: async () => {
            if(queryParams.get('emailId') === null) {
                const response = await api.get("http://localhost:8080/employee/get/friendListAndMutualFriend");
                return response.data;
            }
            else {
    
                const response = await api.get("http://localhost:8080/employee/get/friendListAndMutualFriend?emailId=" + queryParams.get("emailId"));
                return response.data;
            }
         
        },
        initialData: []
    });

    const [newFeeds, setNewFeeds] = useState([]);

    useEffect(() => {
        if (userPost) {
            setNewFeeds(userPost);
        }
    }, [userPost]);



    if (isLoadingInfo) {
        return <div>Loading...</div>;
    }

    if (isErrorInfo) {
        return <div>Error loading profile data.</div>;
    }

    return (
        <div className="profile">
            <CoverPhoto src={profileUserImg} />
            <div className="userInfo">
                <ProfilePicture src={profileUserImg} />
                <UserInfo name={userData.name} bio={userData.bio} location={userData.location} isFriend={sessionStorage.getItem("isFriend")} />
            </div>
            <PostList posts={newFeeds} friendList={friendList} />
        </div>
    );
};
