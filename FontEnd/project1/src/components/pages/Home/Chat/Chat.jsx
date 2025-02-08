import React, { useState, useEffect, useRef } from "react";
import { Button, Form } from "react-bootstrap";
import { sendMessage } from "../../../../helpers/WebSocketService";
import { AiOutlinePicture } from "react-icons/ai";
import classes from "./Chat.module.css";
import api from "../../../../helpers/api";

const Chat = ({ userId, friend, onClose, messages, chatContainerRef }) => {
  const [message, setMessage] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const [previewUrl, setPreviewUrl] = useState(null); // URL preview
  const messageEndRef = useRef(null);
  const prevScrollHeight = useRef(0);
  const [messagePage, setMessagePage] = useState(0);
  const receiverId = friend.id;
  const fileInputRef = useRef(null);
  // console.log("messages:", messages);
  const handleFileSelect = (event) => {
    const files = event.target.files;
    if (files.length > 0) {
      const file = files[0];
      setSelectedFile(file);
      const url = URL.createObjectURL(file);
      setPreviewUrl(url);
    }
  };

  useEffect(() => {
    if (chatContainerRef.current) {
      const chatContainer = chatContainerRef.current;
      if (messagePage > 0) {
        chatContainer.scrollTop =
          chatContainer.scrollHeight - prevScrollHeight.current;
      } else {
        chatContainer.scrollTop = chatContainer.scrollHeight;
      }

      prevScrollHeight.current = chatContainer.scrollHeight;
    }
  }, [messages]);

  const handleFileUpload = async (file) => {
    const formData = new FormData();
    formData.append("file", file);

    const response = await api.post("http://localhost:8080/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    return response.data;
  };

  const handleSendMessage = async () => {
    let mediaUrl = null;
    if (!message && !selectedFile) {
      alert("Please provide content or select a file.");
      return;
    }
    if (message.trim()) {
      if (selectedFile) {
        mediaUrl = await handleFileUpload(selectedFile);
      }

      sendMessage(message, receiverId, userId, mediaUrl);
      setMessage("");
      setSelectedFile(null);
      setPreviewUrl(null);
    }
  };

  const getSenderName = (msg) => {
    if (msg.senderId === parseInt(userId)) {
      return sessionStorage.getItem("username");
    } else if (msg.senderId === parseInt(receiverId)) {
      return friend.employee.userName;
    }
    return "";
  };

  const renderMessageContent = (msg) => {
    return (
      <div className={classes.messageContent}>
        <span>{msg.content}</span>
        {msg.mediaUrl && (
          <div className={classes.imageContainer}>
            <img
              src={msg.mediaUrl}
              alt="media"
              className={classes.mediaImage}
            />
          </div>
        )}
      </div>
    );
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
              msg.senderId === parseInt(userId)
                ? classes.sent
                : classes.received
            }`}
          >
            <strong>{getSenderName(msg)}:</strong> {renderMessageContent(msg)}
          </div>
        ))}
        <div ref={messageEndRef} />
      </div>
      <div className={classes.footer}>
        {previewUrl && (
          <div className={classes.previewContainer}>
            <img
              src={previewUrl}
              alt="Preview"
              className={classes.previewImage}
            />
            <button
              className={classes.removePreview}
              onClick={() => {
                setPreviewUrl(null);
                setSelectedFile(null);
              }}
            >
              ✖
            </button>
          </div>
        )}

        <div className={classes.media}>
          <Button
            className={classes.media_button}
            variant="light"
            onClick={() => fileInputRef.current.click()}
          >
            <AiOutlinePicture size={20} className="me-2" />
          </Button>
          <input
            type="file"
            ref={fileInputRef}
            style={{ display: "none" }}
            onChange={handleFileSelect}
          />
        </div>
        <Form.Control
          type="text"
          placeholder="Type a message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={(e) => e.key === "Enter" && handleSendMessage()}
          className={classes.input}
        />
        <Button
          variant="primary"
          onClick={handleSendMessage}
          className={classes.sendButton}
        >
          Send
        </Button>
      </div>
    </div>
  );
};

export default Chat;
