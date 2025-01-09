import React, { useState } from "react";
import { FaSearch } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

export const SearchBar = () => {
    const [searchTerm, setSearchTerm] = useState("");
    const navigate = useNavigate();

    const handleSearch = (event) => {
        event.preventDefault(); 
        console.log("Key pressed:", event.key); 
        if (event.key === "Enter" && searchTerm.trim()) {
            alert("dâ")
            const mockResults = [
                {
                    userInfoDTO: {
                        emailId: "user1@example.com",
                        employee: { userName: "User 1" },
                        accName: "user1",
                    },
                    isFriend: false,
                    isFollowed: true,
                    mutualFriend: 2,
                },
                {
                    userInfoDTO: {
                        emailId: "user2@example.com",
                        employee: { userName: "User 2" },
                        accName: "user2",
                    },
                    isFriend: true,
                    mutualFriend: 5,
                },
            ];

            navigate("/search-results", { state: { results: mockResults } });
        }
    };

    return (
        <input
            type="text"
            placeholder="Search user..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyUp={handleSearch}
        />
    );
};
