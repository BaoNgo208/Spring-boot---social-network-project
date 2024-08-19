import React, { useEffect, useState } from 'react';
import { useQuery } from "@tanstack/react-query";
import CoverPhoto from './CoverPhoto';
import ProfilePicture from './ProfilePicture';
import UserInfo from './UserInfo';
import PostList from './PostList';
import { userData } from './data';
import api from '../../../helpers/api';
import { useInfiniteQuery } from "@tanstack/react-query";
import profileUserImg from '../../../assests/woman.jpg';
import profileUserImg2 from '../../../assests/man.jpg';
import { useLocation } from 'react-router-dom';
import './Profile.css';
import { useCallback } from 'react';
import Post from '../Home/post/Post';


export const Profile = () => {
    const location = useLocation();
    const [newFeeds, setNewFeeds] = useState([]);

    const queryParams = new URLSearchParams(location.search);

    // const { data: userPost, isLoading: isLoadingInfo, isError: isErrorInfo } = useQuery({
    //     queryKey: "userPost",
    //     queryFn: async () => {
    //         if (queryParams.get('emailId') === null) {
    //             const response = await api.get("http://localhost:8080/post/get/profile");
    //             // setPosts(response.data.pages.flat())
    //             return response.data;
    //         } else {
    //             const response = await api.get("http://localhost:8080/post/get/profile?emailId=" + queryParams.get("emailId"));
    //             // setPosts(response.data.pages.flat())
    //             return response.data;
    //         }
    //     }
    // });


    const {
        data,
        fetchNextPage,
        hasNextPage,
        isFetchingNextPage,
        isLoading: isLoadingInfo,
        isError: isErrorInfo,
      } = useInfiniteQuery({
        queryKey: "posts",
        queryFn: async ({ pageParam = 0 }) => {

          if (queryParams.get('emailId') === null) {
            const response = await api.get(`http://localhost:8080/post/get/profile?
            &page=${pageParam}
            &size=4`);
            return response.data;
        } else {
            const response = await api.get(`http://localhost:8080/post/get/profile?emailId=${queryParams.get("emailId")}
            &page=${pageParam}
            &size=4`);
            return response.data;
        }
        },
        getNextPageParam: (lastPage, allPages) => {
          const nextPage = allPages.length;
          return lastPage.length === 0 ? undefined : nextPage;
        },
      });
    
    useEffect(() => {
    if (data) {
        setNewFeeds(data.pages.flat());
  
    }
    }, [data]);

    const handleScroll = useCallback(() => {
    if (
        window.innerHeight + document.documentElement.scrollTop !==
        document.documentElement.offsetHeight ||
        isFetchingNextPage ||
        !hasNextPage
    ) {
        return;
    }
    fetchNextPage();
    }, [fetchNextPage, isFetchingNextPage, hasNextPage]);


   

    useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
    }, [handleScroll]);


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


    // useEffect(() => {
    //     if (userPost) {
    //         setNewFeeds(userPost);
    //     }
    // }, [userPost]);



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

            <div className="content-container" >

                <div className='friendList'>
                    {/* <h3>Bạn bè</h3>
                    <div>{friends.length}</div> */}
                    {friendList.map((friend, index) => (
                        <div key={index} className="friendItem">
                            <img src={profileUserImg} alt="Friend Profile" />
                            <div >{friend.userInfoDTO.employee.userName}</div>
                            <div >{friend.mutualFriend} bạn chung</div>
                        </div>
                    ))}
                </div>

                <Post  posts={newFeeds} friendList={friendList} className="userPost" />

            </div>
        
            
        </div>
    );
};
