import React, { useState, useEffect, useRef, useCallback } from 'react';
import { Button, Form } from 'react-bootstrap';
import { sendMessage } from '../../../../helpers/WebSocketService';
import classes from "./Chat.module.css";

const Chat = ({ userId, friend, onClose, messages, chatContainerRef }) => {
  const [message, setMessage] = useState('');
  const messageEndRef = useRef(null);
  const prevScrollTop = useRef(0);
  const [messagePage, setMessagePage] = useState(0); // Khởi tạo messagePage
  const prevScrollHeight = useRef(0);
  const receiverId = friend.id;

  useEffect(() => {
    if (chatContainerRef.current) {
      const chatContainer = chatContainerRef.current;
  
      if (messagePage > 0) {
        // Giữ nguyên vị trí cuộn
        chatContainer.scrollTop = chatContainer.scrollHeight - prevScrollHeight.current;
      } else {
        // Cuộn xuống cuối khi mở chat lần đầu
        chatContainer.scrollTop = chatContainer.scrollHeight;
      }
  
      // Cập nhật chiều cao cuộn hiện tại cho lần cuộn tiếp theo
      prevScrollHeight.current = chatContainer.scrollHeight;
    }
  }, [messages]);

  const handleSendMessage = () => {
    if (message.trim()) {
      sendMessage(message, receiverId, userId);
      setMessage('');
    }
  };

  const getSenderName = (msg) => {
    if (msg.senderId === parseInt(userId)) {
      return sessionStorage.getItem("username");
    } else if (msg.senderId === parseInt(receiverId)) {
      return friend.employee.userName;
    }
    return '';
  };

  return (
    <div className={classes.chatWindow}>
      <div className={classes.header}>
        <span>{friend.employee.userName}</span>
        <button onClick={onClose} className={classes.closeButton}>
          Close
        </button>
      </div>
      <div ref={chatContainerRef} className={classes.messageContainer}>
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`${classes.message} ${
              msg.senderId === parseInt(userId) ? classes.sent : classes.received
            }`}
          >
            <strong>{getSenderName(msg)}:</strong> {msg.content}
          </div>
        ))}
        <div ref={messageEndRef} />
      </div>
      <div className={classes.footer}>
        <Form.Control
          type="text"
          placeholder="Type a message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
          className={classes.input}
        />
        <Button variant="primary" onClick={handleSendMessage} className={classes.sendButton}>
          Send
        </Button>
      </div>
    </div>
  );
};

export default Chat;
