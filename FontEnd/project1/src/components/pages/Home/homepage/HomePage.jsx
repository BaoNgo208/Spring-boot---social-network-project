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
import { useRef } from "react";

export const Home = () => {
  const [unreadMessages, setUnreadMessages] = useState({});
  const [allMessages, setAllMessages] = useState([]);
  const [selectedFriend, setSelectedFriendState] = useState(null);
  const [newFeeds, setNewFeeds] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const { setPosts } = usePostContext();
  const chatContainerRef = useRef(null);
  const [messagePage, setMessagePage] = useState(0);

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
      return lastPage.length === 0 ? undefined : nextPage;
    },
  });

  useEffect(() => {
    if (data) {
      const posts = data.pages.flat();
      setNewFeeds(posts);
      setPosts(posts);
    }
  }, [data, setPosts]);

  const handlePost = (newPost) => {
    setNewFeeds((prevFeeds) => [newPost, ...prevFeeds]);
  };

  const loadMessages = async (friend, page) => {
    const senderId = sessionStorage.getItem("userId");
    const response = await api.get(
      `http://localhost:8080/get/messages?userId1=${senderId}&userId2=${friend.id}&page=${page}&size=8&sort=timestamp,desc`
    );
    const messages = response.data.content;
  
    if (messages && messages.length > 0) {
      return messages;
    } else {
      setHasMore(false); // Dừng fetch nếu không còn tin nhắn nào
      return []; // Trả về mảng rỗng nếu không tìm thấy tin nhắn nào
    }
  };

  const handleFriendClick = async (friend) => {
    setSelectedFriendState(friend);
    setSelectedFriend(friend.id);

    const latestMessages = await loadMessages(friend, 0);
    setAllMessages(latestMessages);

    setMessagePage(1);
    setHasMore(latestMessages.length > 0);

    setUnreadMessages((prevUnreadMessages) => ({
      ...prevUnreadMessages,
      [friend.id]: 0,
    }));
  };

  const handleCloseChat = () => {
    setSelectedFriendState(null);
    setSelectedFriend(null);
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

  const loadMoreMessages = async () => {
    if (!selectedFriend || !hasMore) return;
  
    const olderMessages = await loadMessages(selectedFriend, messagePage);
  
    if (olderMessages.length > 0) {
      // Lọc các tin nhắn đã tồn tại trong allMessages
      const newMessages = olderMessages.filter(
        (msg) => !allMessages.some(existingMsg => existingMsg.id === msg.id)
      );
  
      if (newMessages.length > 0) {
        setAllMessages((prevMessages) => [...newMessages, ...prevMessages]);
        setMessagePage((prevPage) => prevPage + 1);
      } else {
        setHasMore(false);
      }
    } else {
      setHasMore(false);
    }
  };

  const handleChatScroll = useCallback(() => {
    if (chatContainerRef.current.scrollTop === 0 && hasMore) {
      loadMoreMessages();
    }
  }, [hasMore, loadMoreMessages]);
  
  useEffect(() => {
    if (selectedFriend) {
      const chatContainer = chatContainerRef.current;
      chatContainer.addEventListener("scroll", handleChatScroll);

      return () => {
        chatContainer.removeEventListener("scroll", handleChatScroll);
      };
    }
  }, [selectedFriend, handleChatScroll]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [handleScroll]);

  const sortedMessages = allMessages.sort(
    (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
  );

  if (isLoadingInfo || isLoadingPosts) return <div>Loading...</div>;
  if (isErrorInfo || isErrorPosts) return <div>Error fetching data</div>;

  const selectedFriendMessages = sortedMessages.filter(
    (msg) =>
      (msg.senderId === parseInt(selectedFriend?.id) &&
        msg.receiverId === parseInt(sessionStorage.getItem("userId"))) ||
      (msg.senderId === parseInt(sessionStorage.getItem("userId")) &&
        msg.receiverId === parseInt(selectedFriend?.id))
  );

  return (
      <div>
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
                  messages={selectedFriendMessages} // Use sorted messages here
                  className={classes.chat}
                  chatContainerRef={chatContainerRef}
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
  );
};