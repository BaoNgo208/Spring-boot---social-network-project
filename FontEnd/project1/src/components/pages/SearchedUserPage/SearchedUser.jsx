import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import profileUserImg from "../../../assests/woman.jpg";
import api from "../../../helpers/api";
import "./SearchedUser.css";

export const SearchedUser = ({ result }) => {
  const navigate = useNavigate();
  const [friendRequestSent, setFriendRequestSent] = useState(false);

  if (!result) {
    return (
      <div className="no-result">
        <p>Không tìm thấy người dùng nào.</p>
      </div>
    );
  }

  const handleProfile = () => {
    const encodedResult = encodeURIComponent(JSON.stringify(result));
    const queryParams = new URLSearchParams({
      emailId: result.emailId,
      user: encodedResult,
    });

    navigate(`/profile?${queryParams.toString()}`);
  };

  const handleAddFriend = async (accName) => {
    try {
      await api.post(`http://localhost:8080/employee/addFriend/${accName}`);
      setFriendRequestSent(true);
    } catch (error) {
      console.error("Error sending friend request:", error);
    }
  };

  return (
    <div className="searched-user-result">
      {/* Ảnh đại diện */}
      <img src={profileUserImg} className="profileUserImg" alt="Profile" />

      {/* Thông tin người dùng */}
      <div className="info">
        <div className="user-info">
          <span>{result.user.userName}</span>
          <span className="acc-name">
            @{result.accName} {result.friend ? ", bạn bè" : ""}
          </span>

          <span className="accName">
            {result.accName === sessionStorage.getItem("accName")
              ? ""
              : result.mutualFriend + " bạn chung"}
          </span>
        </div>
      </div>

      {/* Nút hành động */}
      <div className="add-friend-button">
        {result.friend ? (
          <button className="action-button" onClick={handleProfile}>
            Xem trang cá nhân
          </button>
        ) : friendRequestSent ? (
          <button className="action-button" disabled>
            Đã gửi lời mời
          </button>
        ) : (
          <button
            className="action-button"
            onClick={() => handleAddFriend(result.accName)}
          >
            Thêm bạn bè
          </button>
        )}
      </div>
    </div>
  );
};
