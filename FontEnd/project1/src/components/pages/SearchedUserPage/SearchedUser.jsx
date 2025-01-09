import React from "react";
import { useNavigate } from "react-router-dom";
import profileUserImg from "../../../assests/woman.jpg";
import "./SearchedUser.css";

export const SearchedUser = ({ result }) => {
  const navigate = useNavigate();
  if (!result) {
    // Xử lý khi không có kết quả
    return (
      <div className="no-result">
        <p>Không tìm thấy người dùng nào.</p>
      </div>
    );
  }

  const handleProfile = () => {
    const queryParams = new URLSearchParams({
      emailId: result.emailId,
    });
    sessionStorage.setItem("isFriend", result.isFriend);
    navigate(`/profile?${queryParams.toString()}`);
  };

  const handleAddFriend = () => {
    console.log("Thêm bạn bè");
  };

  return (
    <div className="searched-user-result" onClick={handleProfile}>
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
        ) : (
          <button className="action-button" onClick={handleAddFriend}>
            Thêm bạn bè
          </button>
        )}
      </div>
    </div>
  );
};
