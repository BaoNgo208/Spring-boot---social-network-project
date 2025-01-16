import React from "react";
import classes from "./suggestedUsers.module.css";
import profileUserImg from "../../../../assests/woman.jpg";
import man from "../../../../assests/man.jpg";

const SuggestedUsers = (props) => {
  if (!props.recommendUsers) {
    return null;
  }
  console.log(props.recommendUsers);

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
                  <span className={classes.suggestedMsg}>Suggested to you</span>
                </div>
                <button className={classes.followBtn}>Follow</button>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default SuggestedUsers;
