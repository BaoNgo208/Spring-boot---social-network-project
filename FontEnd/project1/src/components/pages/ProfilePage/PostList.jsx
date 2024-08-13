import React from 'react';
import UserPost from './UserPost/UserPost';
import profileUserImg from '../../../assests/man.jpg';
import classes from './PostList.module.css';

const PostList = (props) => {
    const friends = props.friendList || []; // Ensure friendList is not undefined
    console.log(friends)

    return (
        <div className={classes.container}>
            <div className={classes.friendsList}>
                {/* <h3>Bạn bè</h3>
                <div>{friends.length}</div> */}
                {friends.map((friend, index) => (
                    <div key={index} className={classes.friend}>
                        <img src={profileUserImg} alt="Friend Profile" />
                        <div className={classes.name}>{friend.userInfoDTO.employee.userName}</div>
                        <div className={classes.mutualFriends}>{friend.mutualFriend} bạn chung</div>
                    </div>
                ))}
            </div>
            <div className="post-list">
                {props.posts.map((post, index) => (
                    <UserPost key={index} post={post} />
                ))}
            </div>
        </div>
    );
};

export default PostList;
