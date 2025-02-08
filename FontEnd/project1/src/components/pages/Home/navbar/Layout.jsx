import React, { useRef } from "react";
import Navbar from "./navbar";
import Chat from "../Chat/Chat";
import { useChat } from "../../../../helpers/context/ChatContext";
import { useSelectedFriendMessagesContext } from "../../../../../src/helpers/context/SelectedFriendMessagesContext";
import { useState, useCallback, useEffect } from "react";
import api from "../../../../helpers/api";
import { registerMessageCallback } from "../../../../helpers/WebSocketService";
import { useFriendContext } from "../../../../helpers/context/FriendContext";
const Layout = ({ children }) => {
  const { selectedFriend, handleCloseChat } = useChat();
  const chatContainerRef = useRef(null);
  const [unreadMessages, setUnreadMessages] = useState({});
  const { messages, setMessages } = useSelectedFriendMessagesContext();
  const { setFriends, setContextAllMessages } = useFriendContext();

  const [messagePage, setMessagePage] = useState(0);
  const [allMessages, setAllMessages] = useState([]);

  const [hasMore, setHasMore] = useState(true);
  useEffect(() => {
    registerMessageCallback((messageObject) => {
      setUnreadMessages(messageObject.unreadMessages);
      setAllMessages((prevMessages) => [
        ...prevMessages,
        messageObject.message,
      ]);
      console.log("all message", allMessages);
      setContextAllMessages(allMessages);
    });
  }, []);

  const loadMessages = async (friend, page) => {
    const senderId = sessionStorage.getItem("userId");
    const response = await api.get(
      `http://localhost:8080/get/messages?userId1=${senderId}&userId2=${friend.id}&page=${page}&size=8&sort=timestamp,desc`
    );
    const messages = response.data.content;

    if (messages && messages.length > 0) {
      return messages;
    } else {
      setHasMore(false);
      return [];
    }
  };

  const loadMoreMessages = async () => {
    if (!selectedFriend || !hasMore) return;

    const olderMessages = await loadMessages(selectedFriend, messagePage);
    if (olderMessages.length > 0) {
      const newMessages = olderMessages.filter(
        (msg) => !allMessages.some((existingMsg) => existingMsg.id === msg.id)
      );

      if (newMessages.length > 0) {
        setAllMessages((prevMessages) => [...newMessages, ...prevMessages]);
        setMessagePage((prevPage) => {
          console.log("Updated messagePage:", prevPage + 1);
          return prevPage + 1;
        });
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

  const sortedMessages = allMessages.sort(
    (a, b) => new Date(a.createdAt) - new Date(b.createdAt)
  );
  useEffect(() => {
    if (selectedFriend) {
      const selectedFriendMessages = sortedMessages.filter(
        (msg) =>
          (msg.senderId === parseInt(selectedFriend.id) &&
            msg.receiverId === parseInt(sessionStorage.getItem("userId"))) ||
          (msg.senderId === parseInt(sessionStorage.getItem("userId")) &&
            msg.receiverId === parseInt(selectedFriend.id))
      );

      setMessages(selectedFriendMessages);
    } else {
      setMessages([]); // Nếu không có bạn bè nào được chọn, làm rỗng danh sách tin nhắn
    }
  }, [selectedFriend, sortedMessages]);
  return (
    <div>
      <div
        style={{
          zIndex: 1000,
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          backgroundColor: "#fff",
          boxShadow: "0 2px 5px rgba(0, 0, 0, 0.1)",
        }}
      >
        <Navbar />
      </div>

      <div style={{ marginTop: "60px" }}>{children}</div>

      {selectedFriend && (
        <div
          style={{
            position: "fixed",
            bottom: 0,
            right: 20,
            zIndex: 1100,
            width: "350px",
            maxHeight: "500px",
            overflow: "hidden",
            backgroundColor: "#fff",
            borderRadius: "10px",
          }}
        >
          <Chat
            userId={sessionStorage.getItem("userId")}
            friend={selectedFriend}
            onClose={handleCloseChat}
            messages={messages}
            chatContainerRef={chatContainerRef}
          />
        </div>
      )}
    </div>
  );
};

export default Layout;
