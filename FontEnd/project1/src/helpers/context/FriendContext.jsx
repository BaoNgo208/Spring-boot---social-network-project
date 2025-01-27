import React, { createContext, useContext, useState, useEffect } from "react";
import api from "../api";
const FriendContext = createContext();

export const FriendProvider = ({ children }) => {
  const [friends, setFriends] = useState([]);
  const [contextAllMessages, setContextAllMessages] = useState([]);

  return (
    <FriendContext.Provider
      value={{
        friends,
        setFriends,
        contextAllMessages,
        setContextAllMessages,
      }}
    >
      {children}
    </FriendContext.Provider>
  );
};

export const useFriendContext = () => useContext(FriendContext);
