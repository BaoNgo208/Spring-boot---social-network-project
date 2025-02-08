import { useState, useEffect, useRef } from "react";
import classes from "./ChatDropdown.module.css";
import profileUserImg from "../../../../../assests/man.jpg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMessage } from "@fortawesome/free-solid-svg-icons";
import { useInfiniteQuery } from "@tanstack/react-query";
import api from "../../../../../helpers/api";
import { useChat } from "../../../../../helpers/context/ChatContext";
import { useSelectedFriendMessagesContext } from "../../../../../helpers/context/SelectedFriendMessagesContext";
const ChatDropdown = (props) => {
  const [isOpen, setIsOpen] = useState(false);
  const [chatTimes, setChatTimes] = useState({});
  const chatContainerRef = useRef(null);
  const { setSelectedFriend } = useChat();
  const { loadMessages } = useSelectedFriendMessagesContext();
  const {
    data: chats,
    fetchNextPage,
    hasNextPage,
    isLoading,
    isError,
  } = useInfiniteQuery({
    queryKey: ["chats"],
    queryFn: async ({ pageParam = 0 }) => {
      try {
        const response = await api.get(
          `http://localhost:8080/get/chat?userId=${sessionStorage.getItem(
            "userId"
          )}&userName=${sessionStorage.getItem(
            "username"
          )}&page=${pageParam}&size=4`
        );
        return response.data;
      } catch (error) {
        console.error("Error fetching chat:", error);
        throw new Error("Failed to fetch chats.");
      }
    },
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = allPages.length;
      return lastPage.length === 0 ? undefined : nextPage;
    },
    select: (data) => ({
      content: data.pages.flatMap((page) => page.content),
    }),
    enabled: isOpen,
  });

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleChatClicked = (otherUserId) => {
    const friend = props.friends.find((f) => f.id === otherUserId);
    if (friend) {
      setSelectedFriend(friend); // Cập nhật selectedFriend nếu tìm thấy
      loadMessages(friend, 0);
    } else {
      console.log("User not found in friends list.");
    }
  };

  const calculateTimeElapsed = (createdAt) => {
    const now = Date.now();
    const diff = now - new Date(createdAt).getTime();

    const minutes = Math.floor(diff / (1000 * 60));
    if (minutes < 60) return `${minutes} phút trước`;

    const hours = Math.floor(diff / (1000 * 60 * 60));
    if (hours < 24) return `${hours} giờ trước`;

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    if (days < 365) return `${days} ngày trước`;

    const years = Math.floor(days / 365);
    return `${years} năm trước`;
  };

  useEffect(() => {
    if (isOpen && chats?.content) {
      const updateTimes = () => {
        const newTimes = {};
        chats.content.forEach((chat) => {
          if (chat.chat.lastMessage?.createdAt) {
            newTimes[chat.chat.id] = calculateTimeElapsed(
              chat.chat.lastMessage.createdAt
            );
          } else {
            newTimes[chat.chat.id] = "Vừa xong";
          }
        });
        setChatTimes(newTimes);
      };

      updateTimes();
      const interval = setInterval(updateTimes, 60000);

      return () => clearInterval(interval);
    }
  }, [isOpen, chats]);

  // Xử lý khi người dùng cuộn xuống cuối danh sách
  const handleScroll = () => {
    if (chatContainerRef.current) {
      const { scrollTop, scrollHeight, clientHeight } =
        chatContainerRef.current;
      if (scrollTop + clientHeight >= scrollHeight - 10) {
        if (hasNextPage) {
          fetchNextPage();
        }
      }
    }
  };

  return (
    <div className={classes.dropdown}>
      <button onClick={toggleDropdown} className={classes.dropdownToggle}>
        <FontAwesomeIcon icon={faMessage} />
      </button>
      {isOpen && (
        <div className={classes.dropdownMenu}>
          <h4 className={classes.header}>Đoạn chat</h4>
          <input
            type="text"
            placeholder="Tìm kiếm trên Messenger"
            className={classes.searchBox}
          />

          <div
            ref={chatContainerRef}
            className={classes.chatList}
            onScroll={handleScroll}
          >
            {isLoading ? (
              <p>Loading chats...</p>
            ) : isError ? (
              <p>Something went wrong while fetching chats.</p>
            ) : chats?.content?.length > 0 ? (
              chats.content.map((chat) => {
                const currentUserId = Number(sessionStorage.getItem("userId")); // Lấy userId trong session
                const participants = chat.chat.participants; // Danh sách user trong cuộc trò chuyện
                const otherUserId = participants.find(
                  (id) => id !== currentUserId
                ); // Lấy ID của người còn lại

                const otherUserName =
                  chat.users?.[otherUserId] || "Người dùng ẩn danh";

                return (
                  <div
                    key={chat.chat.id}
                    className={classes.chatItem}
                    onClick={() => handleChatClicked(otherUserId)}
                  >
                    <img
                      src={profileUserImg || "/default-avatar.png"}
                      alt="Avatar"
                      className={classes.avatar}
                    />
                    <div className={classes.chatDetails}>
                      <p className={classes.chatName}>{otherUserName}</p>{" "}
                      <p className={classes.chatMessage}>
                        {chat.chat.lastMessage?.content ||
                          "Đã gửi một tin nhắn"}
                      </p>
                    </div>
                    <p className={classes.chatTime}>
                      {chatTimes[chat.chat.id] || "Vừa xong"}
                    </p>
                  </div>
                );
              })
            ) : (
              <p className={classes.noChat}>Không có tin nhắn nào.</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default ChatDropdown;
