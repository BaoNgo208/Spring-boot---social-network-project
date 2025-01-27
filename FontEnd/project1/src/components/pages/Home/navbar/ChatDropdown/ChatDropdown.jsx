import { useState, useEffect } from "react";
import classes from "./ChatDropdown.module.css";
import profileUserImg from "../../../../../assests/man.jpg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faMessage } from "@fortawesome/free-solid-svg-icons";
import { useFriendContext } from "../../../../../helpers/context/FriendContext";
const ChatDropdown = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [chats, setChats] = useState([]);

  const { friends } = useFriendContext();
  console.log("friend:", friends);
  const sampleChats = [
    {
      id: 1,
      senderName: "Duy lại xấp xỉnh",
      lastMessage: "Xin chào, bạn có khỏe không?",
      timeElapsed: "14 phút trước",
    },
    {
      id: 2,
      senderName: "Bé Phụng",
      lastMessage: "Im tóc",
      timeElapsed: "15 giờ trước",
    },
    {
      id: 3,
      senderName: "Châu Huỳnh Anh Quân",
      lastMessage: "Bạn đang làm gì?",
      timeElapsed: "1 ngày trước",
    },
    {
      id: 4,
      senderName: "Phương Dung",
      lastMessage: "Tôi đã bày tỏ cảm xúc 😅",
      timeElapsed: "1 ngày trước",
    },
  ];

  const toggleDropdown = () => {
    if (!isOpen) {
      setChats(sampleChats); // Load dữ liệu mẫu khi mở
    }
    setIsOpen(!isOpen);
  };

  const handleChatClicked = (chatId) => {
    console.log(`Navigate to chat with ID: ${chatId}`);
    // Xử lý điều hướng đến trang chi tiết chat
  };

  return (
    <div className={classes.dropdown}>
      <button onClick={toggleDropdown} className={classes.dropdownToggle}>
        <FontAwesomeIcon icon={faMessage} />
      </button>
      {isOpen && (
        <div className={classes.dropdownMenu}>
          {chats.length > 0 ? (
            chats.map((chat) => (
              <div
                className={classes.container}
                key={chat.id}
                onClick={() => handleChatClicked(chat.id)}
              >
                <div className={classes.dropdownItem}>
                  <div>
                    <img
                      src={profileUserImg}
                      className={classes.profileUserImg}
                      alt="Profile"
                    />
                  </div>
                  <div className={classes.userInfo}>
                    <div className={classes.senderName}>{chat.senderName}</div>
                    <div className={classes.lastMessage}>
                      {chat.lastMessage}
                    </div>
                    <div className={classes.timeElapsed}>
                      {chat.timeElapsed}
                    </div>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className={classes.noChats}>No chats</div>
          )}
        </div>
      )}
    </div>
  );
};

export default ChatDropdown;
