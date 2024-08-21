import React, { useState, useEffect ,useCallback} from "react";
import Navbar from "../navbar/navbar";
import Rightside from "../rightside/Rightside";
import SuggestedUsers from "../suggestedUsers/SuggestedUsers";
import Post from "../post/Post";
import classes from "./home.module.css";
import { useQuery } from "@tanstack/react-query";
import api from "../../../../helpers/api";
import PostSection from "../posting/posting";
import Chat from "../Chat/Chat";
import { useInfiniteQuery } from "@tanstack/react-query";
import { registerMessageCallback, setSelectedFriend } from "../../../../helpers/WebSocketService";
import { NotificationProvider } from "../navbar/Notification/NotificationContext";
import { usePostContext } from "../post/PostDetail/PostContext";
export const Home = () => {
  const [unreadMessages, setUnreadMessages] = useState({});
  const [allMessages, setAllMessages] = useState([]);
  const [selectedFriend, setSelectedFriendState] = useState(null);
  const [newFeeds, setNewFeeds] = useState([]);
  const { setPosts } = usePostContext(); // Lấy hàm setPosts từ context

  useEffect(() => {
    registerMessageCallback((messageObject) => {
      setUnreadMessages(messageObject.unreadMessages);
      setAllMessages((prevMessages) => [...prevMessages, messageObject.message]);
    });
  }, []);

  const {
    data: info,
    isLoading: isLoadingInfo,
    isError: isErrorInfo,
  } = useQuery({
    queryKey: "info",
    queryFn: async () => {
      const response = await api.get("http://localhost:8080/employee/get/friendList");
      return response.data;
    },
  });

  const {
    data,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading: isLoadingPosts,
    isError: isErrorPosts,
  } = useInfiniteQuery({
    queryKey: "posts",
    queryFn: async ({ pageParam = 0 }) => {
      const response = await api.get(
        `http://localhost:8080/post/get/recommend/post?page=${pageParam}&size=4`
      );
      return response.data;
    },
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = allPages.length;
      console.log("page size:"+allPages.length)
      return lastPage.length === 0 ? undefined : nextPage;
    },
  });

  useEffect(() => {
    if (data) {
      const posts = data.pages.flat();
      setNewFeeds(data.pages.flat());
      setPosts(posts);
    }
  }, [data]);

  const handlePost = (newPost) => {
    setNewFeeds((prevFeeds) => [newPost, ...prevFeeds]);
  };

  const handleFriendClick = (friend) => {
    setSelectedFriendState(friend);
    setSelectedFriend(friend.id); // Cập nhật ID của người bạn được chọn trong WebSocketService

    // Reset số lượng tin nhắn chưa đọc cho người bạn được chọn
    setUnreadMessages((prevUnreadMessages) => ({
      ...prevUnreadMessages,
      [friend.id]: 0,
    }));
  };

  const handleCloseChat = () => {
    setSelectedFriendState(null);
    setSelectedFriend(null); // Không có người bạn nào được chọn
  };

  useEffect(() => {
    if (isErrorInfo || isErrorPosts) {
      window.location.href = "/";
    }
  }, [isLoadingInfo, isLoadingPosts, isErrorInfo, isErrorPosts]);

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

  if (isLoadingInfo || isLoadingPosts) return <div>Loading...</div>;
  if (isErrorInfo || isErrorPosts) return <div>Error fetching data</div>;

  const selectedFriendMessages = allMessages.filter(
    (msg) =>
      (msg.senderId === parseInt(selectedFriend?.id) &&
        msg.receiverId === parseInt(sessionStorage.getItem("userId"))) ||
      (msg.senderId === parseInt(sessionStorage.getItem("userId")) &&
        msg.receiverId === parseInt(selectedFriend?.id))
  );

  return (
    <NotificationProvider> {/* Bọc các component bên trong NotificationProvider */}

    <div>
      {/* <Navbar style={{ zIndex: "1", position: "fixed" , top: 0, left: 0}} /> */}
      <div className={classes.container}>
        
        <div className={classes.left}>
          <SuggestedUsers />
        </div>

        <div className="content" style={{ zIndex: "10" }}>
          <PostSection
            className="posting"
            newfeeds={newFeeds}
            onPost={handlePost}
            style={{ position: "relative", zIndex: "1" }}
          />
          <Post posts={newFeeds} />
          <div className={classes.chatSection}>
            {selectedFriend && (
              <Chat
                userId={sessionStorage.getItem("userId")}
                friend={selectedFriend}
                onClose={handleCloseChat}
                messages={selectedFriendMessages}
                style={{ height: "1000px" }}
                className={classes.chat}
              />
            )}
          </div>
        </div>

        <div className={classes.rightside}>
            <Rightside
              friends={info}
              onFriendClick={handleFriendClick}
              unreadMessages={unreadMessages}
            />
      </div>
      </div>
    </div>
  </NotificationProvider>

  );
};