import React, { useState } from "react";
import {
  FaUserFriends,
  FaClock,
  FaBookmark,
  FaUsers,
  FaVideo,
  FaStore,
  FaNewspaper,
} from "react-icons/fa";
import SuggestedUsers from "../suggestedUsers/SuggestedUsers";
import "./Sidebar.css";
const Sidebar = ({ recommendUsers }) => {
  const [activeItem, setActiveItem] = useState("menu");

  const renderContent = () => {
    switch (activeItem) {
      case "friends":
        return (
          <div>
            <div className="back-button" onClick={() => setActiveItem("menu")}>
              🔙 Quay lại
            </div>
            <SuggestedUsers recommendUsers={recommendUsers} />
          </div>
        );
      default:
        return (
          <div className="sidebar-menu">
            <div
              className="sidebar-item"
              onClick={() => setActiveItem("friends")}
            >
              <FaUserFriends /> Bạn bè
            </div>
            <div className="sidebar-item">
              <FaClock /> Kỷ niệm
            </div>
            <div className="sidebar-item">
              <FaBookmark /> Đã lưu
            </div>
            <div className="sidebar-item">
              <FaUsers /> Nhóm
            </div>
            <div className="sidebar-item">
              <FaVideo /> Video
            </div>
            <div className="sidebar-item">
              <FaStore /> Marketplace
            </div>
            <div className="sidebar-item">
              <FaNewspaper /> Bảng feed
            </div>
          </div>
        );
    }
  };

  return <div className="sidebar-container">{renderContent()}</div>;
};

export default Sidebar;
