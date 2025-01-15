import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import api from "../../../helpers/api";
const UserInfo = (props) => {
  const handleFriendCall = async () => {
    try {
      await api.post(
        `http://localhost:8080/employee/addFriend/${props.userInfo.accName}`
      );
    } catch (err) {}
  };

  console.log("userInfo:", props.userInfo);
  return (
    <div className="userInfoContainer">
      <div className="user-info">
        <h1>{props.userInfo.user.userName}</h1>
        <p>{props.numOfFriend} bạn bè</p>
      </div>

      {props.userInfo.emailId !== sessionStorage.getItem("email") && (
        <div className="addFriend">
          <button className="btn btn-primary" onClick={handleFriendCall}>
            {props.isFriend === "true" ? "Bạn Bè" : "Kết Bạn"}
          </button>
          <button className="btn btn-primary">Nhắn tin</button>
        </div>
      )}
    </div>
  );
};

export default UserInfo;
