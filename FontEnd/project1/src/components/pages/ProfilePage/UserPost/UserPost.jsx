import React, { useState, useEffect } from 'react';
import classes from './UserPost.module.css';
import { HiOutlineDotsVertical } from 'react-icons/hi';
import { AiOutlineHeart } from 'react-icons/ai';
import { BiMessageRounded } from 'react-icons/bi';
import { BsBookmark } from 'react-icons/bs';
import profileUserImg from '../../../../assests/woman.jpg'
import api from '../../../../helpers/api';
const UserPost = (props) => {

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString('en-GB', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit' });
  };

  const [commentInput, setCommentInput] = useState('');
  const [post, setPost] = useState(props.post);

  useEffect(() => {
    setPost(props.post);
  }, [props.post]);

  const handleCommentSubmit = async (postId, commentContent) => {
    try {
      const newComment = { content: commentContent, commentTime: new Date() };
  

      const res = await api.post(`http://localhost:8080/post/comment/${postId}`, newComment);
      const resComment = res.data.comment;

      if (post.id === postId) {
        setPost(prevPost => ({
          ...prevPost,
          comment:  resComment
        }));
      }

      setCommentInput('');
    } catch (error) {
      console.error('Error submitting comment:', error);
    }
  };

  return (
    <div className={classes.userPostList}>
      <div className={classes.container}>
        <div className={classes.wrapper}>
          <div className={classes.top}>
            <div className={classes.topLeft}>
              <img src={profileUserImg} className={classes.profileUserImg} alt="Profile" />
              <div className={classes.profileMetadata}>
                <span>{post.user.employee.userName}</span>
                <span>{formatDateTime(props.post.postTime)}</span>
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
              <AiOutlineHeart />
              <BiMessageRounded />
            </div>
            <div className={classes.controlsRight}>
              <BsBookmark />
            </div>
          </div>

          <div className={classes.comments}>
            {(post.comment || []).map((comment, commentIndex) => (
              <div key={commentIndex} className={classes.comment}>
                <div className={classes.commentLeft}>
                  <img src={profileUserImg} className={classes.commentImg} alt="Commenter" />
                  <div className={classes.commentData}>
                    {comment.user && comment.user.employee && (
                      <span>{comment.user.employee.userName}</span>
                    )}
                    <span className={classes.commentTimeago}>{formatDateTime(comment.commentTime)}</span>
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
              onChange={(e) => { setCommentInput(e.target.value); }}
            />
            <button onClick={(e) => {
              e.preventDefault();
              handleCommentSubmit(post.id, commentInput);
            }}>Post</button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserPost;
