import React from "react";
import classes from "./suggestedUsers.module.css";
import profileUserImg from "../../../../assests/woman.jpg";
import man from "../../../../assests/man.jpg";
import api from "../../../../helpers/api";
import { useState } from "react";

const SuggestedUsers = (props) => {
  const [friendRequestSent, setFriendRequestSent] = useState(false);

  if (!props.recommendUsers) {
    return null;
  }
  console.log(props.recommendUsers);

  const handleAddFriend = async (accName) => {
    try {
      await api.post(`http://localhost:8080/employee/addFriend/${accName}`);
      setFriendRequestSent(true);
    } catch (error) {
      console.error("Error sending friend request:", error);
    }
  };

  return (
    <div className={classes.container}>
      <div className={classes.wrapper}>
        <div className={classes.suggestedUsers}>
          {props.recommendUsers.map((user, index) => {
            return (
              <div className={classes.suggestedUser} key={index}>
                <img src={man} className={classes.imgUser} alt="" />
                <div className={classes.suggestedUserData}>
                  <span>{user.userInfoDTO.employee.userName}</span>
                  <span className={classes.suggestedMsg}>
                    @{user.userInfoDTO.accName}
                  </span>
                  <span className={classes.mutualfriend}>
                    {user.mutualFriend} bạn chung
                  </span>
                </div>
                <button
                  className={classes.followBtn}
                  onClick={() => handleAddFriend(user.userInfoDTO.accName)}
                >
                  Kết bạn
                </button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default SuggestedUsers;
