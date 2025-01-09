import React, { useState, useEffect, useCallback, useRef } from "react";
import classes from "./post.module.css";
import profileUserImg from "../../../../assests/woman.jpg";
import { HiOutlineDotsVertical } from "react-icons/hi";
import { AiOutlineHeart } from "react-icons/ai";
import { BiMessageRounded } from "react-icons/bi";
import { BsBookmark } from "react-icons/bs";
import api from "../../../../helpers/api";
import { registerNotificationCallback } from "../../../../helpers/WebSocketService";
import { useNotifications } from "../navbar/Notification/NotificationContext";

const PostItem = ({
  post,
  formatDateTime,
  handleCommentSubmit,
  commentInput,
  onCommentInputChange,
}) => {
  const [likeCount, setLikeCount] = useState(
    post.likes ? post.likes.length : 0
  );

  const [liked, setLiked] = useState(false);
  const { addNotification } = useNotifications();

  const checkLikedStatus = () => {
    const email = sessionStorage.getItem("email");
    const userHasLiked =
      Array.isArray(post.likes) &&
      post.likes.some((like) => like.user.emailId === email);
    setLiked(userHasLiked);
  };

  // Gọi hàm kiểm tra trạng thái "liked" khi component render lần đầu tiên
  useEffect(() => {
    checkLikedStatus();
  }, [post.likes]);

  useEffect(() => {
    const wrappedHandleNotification = (notification) => {
      const currentUserId = parseInt(sessionStorage.getItem("userId"));

      // const postIdFromNotification = notification.data.postId;
      if (currentUserId === notification.data.receiverId) {
        const newNotification = {
          senderId: notification.data.senderId,
          senderName: notification.data.senderName,
          receiverId: notification.data.receiverId,
          createdAt: new Date(), // Add current date as createdAt
          type: notification.type,
          postId: notification.data.postId,
        };

        switch (notification.type) {
          case "COMMENT":
            addNotification(newNotification);
            break;

          case "LIKE":
            addNotification(newNotification);
            break;

          default:
            console.warn("Unhandled notification type:", notification.type);
        }
      }
    };

    // Register the callback
    registerNotificationCallback(wrappedHandleNotification);

    // Cleanup on component unmount
    return () => {
      registerNotificationCallback(null); // Ensure callback is unregistered
    };
  }, []);

  const handleLikeClick = useCallback(async () => {
    try {
      if (liked) {
        // Unlike post
        setLikeCount((prevCount) => prevCount - 1);
        await api.delete(`http://localhost:8080/post/cancelLike/${post.id}`);
      } else {
        // Like post
        setLikeCount((prevCount) => prevCount + 1);
        await api.post(`http://localhost:8080/post/like/${post.id}`);
      }
      setLiked(!liked);
    } catch (error) {
      console.error("Error liking/unliking post:", error);
    }
  }, [liked, post.id]);
  return (
    <div className={classes.container}>
      <div className={classes.wrapper}>
        <div className={classes.top}>
          <div className={classes.topLeft}>
            <img
              src={profileUserImg}
              className={classes.profileUserImg}
              alt="Profile"
            />
            <div className={classes.profileMetadata}>
              <span>{post.user?.employee?.userName}</span>
              <span>{formatDateTime(post.postTime)}</span>
            </div>
          </div>
          <HiOutlineDotsVertical size={25} />
        </div>

        <div className={classes.center}>
          <div className={classes.desc}>{post.content}</div>
          <img src={profileUserImg} className={classes.postImg} alt="Post" />
        </div>

        <div className={classes.controls}>
          <div className={classes.controlsLeft}>
            <AiOutlineHeart
              onClick={handleLikeClick}
              style={{ color: liked ? "pink" : "black" }}
            />
            <span className={classes.likeCount}>{likeCount}</span>
            <BiMessageRounded />
          </div>
          <div className={classes.controlsRight}>
            <BsBookmark />
          </div>
        </div>

        <div className={classes.comments}>
          {post.comment?.map((comment, commentIndex) => (
            <div key={commentIndex} className={classes.comment}>
              <div className={classes.commentLeft}>
                <img
                  src={profileUserImg}
                  className={classes.commentImg}
                  alt="Commenter"
                />
                <div className={classes.commentData}>
                  {comment.user && comment.user.employee && (
                    <span>{comment.user.employee.userName}</span>
                  )}
                  <span className={classes.commentTimeago}>
                    {formatDateTime(comment.commentTime)}
                  </span>
                </div>
                <div className={classes.commentText}>{comment.content}</div>
              </div>
              <div className={classes.commentRight}>
                <AiOutlineHeart />
                <span>{comment.likes} likes</span>
              </div>
            </div>
          ))}
        </div>

        <div className={classes.postCommentSection}>
          <input
            type="text"
            className={classes.inputSection}
            placeholder="Type here..."
            value={commentInput}
            onChange={(e) => onCommentInputChange(e.target.value)}
          />
          <button
            onClick={(e) => {
              e.preventDefault();
              handleCommentSubmit(post.id, commentInput);
            }}
          >
            Post
          </button>
        </div>
      </div>
    </div>
  );
};

export default PostItem;
