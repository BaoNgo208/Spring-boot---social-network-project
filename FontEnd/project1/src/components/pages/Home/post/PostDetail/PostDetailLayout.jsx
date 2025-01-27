import React from "react";
import Rightside from "../../rightside/Rightside";
import Navbar from "../../navbar/navbar";
const Layout = ({ children, friends, unreadMessages, onFriendClick }) => {
  return (
    <div>
      {/* Navbar */}
      <div
        style={{
          zIndex: 1000,
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          backgroundColor: "#fff",
          boxShadow: "0 2px 5px rgba(0, 0, 0, 0.1)",
        }}
      >
        <Navbar />
      </div>

      {/* Main Content Area */}
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          marginTop: "60px",
          height: "calc(100vh - 60px)", // Full height minus the navbar
        }}
      >
        {/* Main Content */}
        <div
          style={{
            display: "flex",
            flex: 1,
            padding: "20px",
            overflowY: "auto", // Scrollable content
          }}
        >
          {children}

          <Rightside
            friends={friends}
            unreadMessages={unreadMessages}
            onFriendClick={onFriendClick}
          />
        </div>
      </div>
    </div>
  );
};

export default Layout;
