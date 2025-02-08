import React, { createContext, useContext, useState, useEffect } from "react";
const ChatContext = createContext();

export const ChatProvider = ({ children }) => {
  const [selectedFriend, setSelectedFriend] = useState(null);
  const handleCloseChat = () => {
    setSelectedFriend(null);
  };

  return (
    <ChatContext.Provider
      value={{
        selectedFriend,
        setSelectedFriend,
        handleCloseChat,
      }}
    >
      {children}
    </ChatContext.Provider>
  );
};

export const useChat = () => useContext(ChatContext);
