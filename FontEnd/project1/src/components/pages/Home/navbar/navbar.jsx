import React, { useState } from "react";
import classes from "./navbar.module.css";
import { Link, useNavigate } from "react-router-dom";
import { AiOutlineSearch, AiOutlineLogout } from "react-icons/ai";
import api from "../../../../helpers/api";
import axios from "axios";
import { SearchResultsList } from "./SearchResultsList";
import FriendRequestsDropdown from "./FriendRequestsDropdown/FriendRequestsDropdown";
import Notification from "./Notification/Notification";

const Navbar = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState("");
  const [results, setResults] = useState([]);

  const fetchResults = async (value) => {
    if (!value || value.trim() === "") {
      setResults([]);
      return;
    }

    try {
      const response = await api.get(
        "http://localhost:8080/employee/get/friendListAndMutualFriend"
      );
      const matchingUsers = response.data
        .filter((user) =>
          user.userInfoDTO.employee.userName
            .toLowerCase()
            .includes(value.toLowerCase())
        )
        .map((user) => ({ ...user, isFriend: true }));

      const response2 = await api.get(
        "http://localhost:8080/employee/get/getRecommendedFriend"
      );
      const matchingRecommendedFriends = response2.data
        .filter((user) =>
          user.userInfoDTO.employee.userName
            .toLowerCase()
            .includes(value.toLowerCase())
        )
        .map((user) => ({ ...user, isFriend: false }));

      const combinedResults = [...matchingUsers, ...matchingRecommendedFriends];
      setResults(combinedResults);
    } catch (error) {
      console.error("Error fetching results:", error);
    }
  };

  const handleChange = (value) => {
    setInput(value);
    fetchResults(value);
  };

  const handleSearch = (event) => {
    if (event.key === "Enter" && input.trim()) {
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

      if (window.location.pathname === "/search-results") {
        // Nếu đang ở trang SearchedUserPage, chỉ cập nhật state
        navigate("/search-results", {
          replace: true,
          state: { results: mockResults, query: input.trim() },
        });
      } else {
        // Nếu không ở trang SearchedUserPage, điều hướng như bình thường
        navigate("/search-results", {
          state: { results: mockResults, query: input.trim() },
        });
      }
    }
  };

  const handleLogout = async () => {
    try {
      await axios.post(
        "http://localhost:8080/logout",
        {},
        {
          headers: {
            Authorization: "Bearer " + sessionStorage.getItem("cookie"),
          },
        }
      );

      sessionStorage.clear();
      navigate("/");
      window.location.reload();
    } catch (error) {
      console.error("Error logging out:", error);
    }
  };
  const handleProfile = () => {
    const queryParams = new URLSearchParams({
      email: sessionStorage.getItem("email"),
      user: sessionStorage.getItem("userInfo"),
    });
    navigate(`/profile?${queryParams.toString()}`);
  };

  return (
    <div className={classes.container}>
      <div className={classes.wrapper}>
        <div className={classes.left}>
          <Link to="/">SociaPulse</Link>
        </div>
        <div className={classes.center}>
          <input
            type="text"
            placeholder="Search user..."
            value={input}
            onChange={(e) => handleChange(e.target.value)}
            onKeyDown={handleSearch}
          />
          <AiOutlineSearch
            className={classes.searchIcon}
            onClick={() => alert("Search icon clicked")}
          />
          <SearchResultsList style={{ zIndex: 10 }} results={results} />
        </div>
        <div className={classes.right}>
          <FriendRequestsDropdown className={classes.friendrequests} />
          <Notification className={classes.notification} />
          <button className={classes.logoutButton} onClick={handleProfile}>
            <AiOutlineLogout className={classes.logoutButtonIcon} />
            Profile
          </button>
          <button className={classes.logoutButton} onClick={handleLogout}>
            <AiOutlineLogout className={classes.logoutButtonIcon} />
            Log Out
          </button>
        </div>
      </div>
    </div>
  );
};

export default Navbar;
