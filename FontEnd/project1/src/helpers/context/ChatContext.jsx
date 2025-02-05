import React, { createContext, useContext, useState, useEffect } from "react";
import api from "../api";
import {
  registerMessageCallback,
  setSelectedFriend as wsSetSelectedFriend,
} from "../WebSocketService";

const ChatContext = createContext();

export const ChatProvider = ({ children }) => {
  const [selectedFriend, setSelectedFriend] = useState(null);

  return (
    <ChatContext.Provider value={{ selectedFriend, setSelectedFriend }}>
      {children}
    </ChatContext.Provider>
  );
};

export const useChat = () => useContext(ChatContext);
