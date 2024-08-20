import React, { useEffect, useState, useCallback } from 'react';
import { useQuery, useInfiniteQuery } from "@tanstack/react-query";
import CoverPhoto from './CoverPhoto';
import ProfilePicture from './ProfilePicture';
import UserInfo from './UserInfo';
import PostList from './PostList';
import { userData } from './data';
import api from '../../../helpers/api';
import profileUserImg from '../../../assests/woman.jpg';
import profileUserImg2 from '../../../assests/man.jpg';
import { useLocation, useNavigate } from 'react-router-dom';
import './Profile.css';
import Post from '../Home/post/Post';

export const Profile = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const [queryParams] = useState(new URLSearchParams(location.search));
    const [newFeeds, setNewFeeds] = useState([]);

    // Store query params to sessionStorage on every change
    useEffect(() => {
        if (location.search) {
            sessionStorage.setItem('queryParams', location.search);
        }
    }, [location.search]);

    // On component mount, if there are no query params, try to restore them from sessionStorage
    useEffect(() => {
        if (!location.search) {
            const savedQueryParams = sessionStorage.getItem('queryParams');
            if (savedQueryParams) {
                navigate(`${location.pathname}${savedQueryParams}`, { replace: true });
            }
        }
    }, [location.search, navigate]);

    const {
        data,
        fetchNextPage,
        hasNextPage,
        isFetchingNextPage,
        isLoading: isLoadingInfo,
        isError: isErrorInfo,
    } = useInfiniteQuery({
        queryKey: ["posts", queryParams.toString()],
        queryFn: async ({ pageParam = 0 }) => {
            const emailId = queryParams.get('emailId');
            const url = emailId 
                ? `http://localhost:8080/post/get/profile?emailId=${emailId}&page=${pageParam}&size=4`
                : `http://localhost:8080/post/get/profile?page=${pageParam}&size=4`;
            const response = await api.get(url);
            return response.data;
        },
        getNextPageParam: (lastPage, allPages) => {
            return lastPage.length === 0 ? undefined : allPages.length;
        },
    });

    useEffect(() => {
        if (data) {
            setNewFeeds(data.pages.flat());
        }
    }, [data]);

    const handleScroll = useCallback(() => {
        if (
            window.innerHeight + document.documentElement.scrollTop >=
            document.documentElement.offsetHeight - 2 && // Adjust this threshold if needed
            !isFetchingNextPage &&
            hasNextPage
        ) {
            fetchNextPage();
        }
    }, [fetchNextPage, isFetchingNextPage, hasNextPage]);

    useEffect(() => {
        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
    }, [handleScroll]);

    const { data: friendList = [] } = useQuery({
        queryKey: ["friendList", queryParams.toString()],
        queryFn: async () => {
            const emailId = queryParams.get('emailId');
            const url = emailId 
                ? `http://localhost:8080/employee/get/friendListAndMutualFriend?emailId=${emailId}`
                : "http://localhost:8080/employee/get/friendListAndMutualFriend";
            const response = await api.get(url);
            return response.data;
        },
        initialData: []
    });

    if (isLoadingInfo) {
        return <div>Loading...</div>;
    }

    if (isErrorInfo) {
        return <div>Error loading profile data.</div>;
    }

    return (
        <div className="profile">
            <CoverPhoto src={profileUserImg2} className="cover-photo" />
            <div className="userInfo">
                <ProfilePicture src={profileUserImg} />
                <UserInfo name={userData.name} bio={userData.bio} location={userData.location} isFriend={sessionStorage.getItem("isFriend")} />
            </div>

            <div className="content-container">
                <div className='friendList'>
                    {friendList.map((friend, index) => (
                        <div key={index} className="friendItem">
                            <img src={profileUserImg} alt="Friend Profile" />
                            <div>{friend.userInfoDTO.employee.userName}</div>
                            <div>{friend.mutualFriend} bạn chung</div>
                        </div>
                    ))}
                </div>
                <Post posts={newFeeds} friendList={friendList} className="userPost" />
            </div>
        </div>
    );
};