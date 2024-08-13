import React from 'react'
import classes from './profileCard.module.css'
import profileUserImg from '../../../../assests/woman.jpg'
import Cookies from 'js-cookie'



const ProfileCard = (props) => {


    return (
        <div className={classes.container}>
            <div className={classes.wrapper}>
                <div className={classes.top}>
                    <div className={classes.imgContainer}>
                        <img src={profileUserImg} className={classes.profileUserImg} />
                    </div>
                    <div className={classes.usernameAndCreatedAt}>
                        <p><span>Email:</span> {sessionStorage.getItem("email")}</p>
                    </div>
                </div>
                    <hr />
                <div className={classes.bottom}>
                    <p>Followers: <span>{props.info.length}</span></p>
                </div>
            </div>
            <h3 className={classes.myProfile}>My profile</h3>
        </div>
    )
}

export default ProfileCard