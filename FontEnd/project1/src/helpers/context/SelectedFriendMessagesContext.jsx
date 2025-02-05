import React, { createContext, useContext, useState } from "react";
import api from "../api";

const SelectedFriendMessagesContext = createContext();

export const SelectedFriendMessagesProvider = ({ children }) => {
  const [messages, setMessages] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true); // Kiểm tra còn tin nhắn cũ không
  const loadMessages = async (friend, newPage = 0) => {
    if (!friend) return; // Kiểm tra nếu không có bạn bè được chọn

    // Reset tin nhắn và trạng thái khi chọn bạn mới
    if (newPage === 0) {
      setMessages([]); // Xóa tin nhắn cũ trước khi load tin nhắn mới
      setPage(0);
      setHasMore(true);
    }

    if (!hasMore && newPage !== 0) return; // Dừng nếu không còn tin nhắn để tải

    const senderId = sessionStorage.getItem("userId");
    try {
      const response = await api.get(
        `http://localhost:8080/get/messages?userId1=${senderId}&userId2=${friend.id}&page=${newPage}&size=8&sort=timestamp,desc`
      );
      const newMessages = response.data.content;

      setMessages((prev) => [...newMessages.reverse(), ...prev]); // Đảo ngược để tin nhắn mới nằm cuối
      setPage(newPage);
      setHasMore(newMessages.length > 0); // Nếu không còn tin nhắn, dừng load
    } catch (error) {
      console.error("Lỗi khi tải tin nhắn:", error);
    }
  };

  return (
    <SelectedFriendMessagesContext.Provider
      value={{ messages, setMessages, loadMessages, page, hasMore }}
    >
      {children}
    </SelectedFriendMessagesContext.Provider>
  );
};

export const useSelectedFriendMessagesContext = () =>
  useContext(SelectedFriendMessagesContext);
