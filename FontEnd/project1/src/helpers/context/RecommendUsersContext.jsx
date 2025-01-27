import React, { createContext, useContext, useState, useEffect } from "react";
import api from "../api";

const RecommendUsersContext = createContext();

export const useRecommendUsers = () => {
  return useContext(RecommendUsersContext);
};

export const RecommendUsersProvider = ({ children }) => {
  const [recommendUsers, setRecommendUsers] = useState([]);

  useEffect(() => {
    const fetchRecommendUsers = async () => {
      try {
        const response = await api.get(
          "http://localhost:8080/employee/get/getRecommendedFriend"
        );
        const data = response.data;
        setRecommendUsers(data);
      } catch (error) {
        console.error("Error fetching recommend users:", error);
      }
    };

    fetchRecommendUsers();
  }, []);

  return (
    <RecommendUsersContext.Provider value={recommendUsers}>
      {children}
    </RecommendUsersContext.Provider>
  );
};
