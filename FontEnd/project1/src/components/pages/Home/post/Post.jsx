import React, { useState, useEffect } from 'react';
import Comment from '../../../../entites/Comment';
import api from '../../../../helpers/api';
import PostItem from './PostItem';
import classes from './post.module.css'; // Import the CSS module for styling

const Post = ({ posts: propPosts = [], className }) => {
  const [posts, setPosts] = useState([]);
  const [commentInputs, setCommentInputs] = useState({});

  useEffect(() => {
    if (propPosts.length > 0) {
      const existingPostIds = new Set(posts.map(post => post.id));
  
      // Filter out posts that already exist in the state
      const newPosts = propPosts.flatMap(page => page.content).filter(post => !existingPostIds.has(post.id));
  
      // Only update the posts state if there are new posts to add
      if (newPosts.length > 0) {
        setPosts((prevPosts) => [...prevPosts, ...newPosts]);
  
        // Update comment inputs for new posts
        setCommentInputs((prevInputs) => {
          const newInputs = newPosts.reduce((acc, post) => ({
            ...acc,
            [post.id]: ''
          }), {});
          return { ...prevInputs, ...newInputs };
        });
      }
    }
  }, [propPosts]);  // Notice that 'posts' is removed from the dependency array

  const handleCommentSubmit = async (postId, commentContent) => {
    try {
      const newComment = new Comment(commentContent, new Date());
      const res = await api.post(`http://localhost:8080/post/comment/${postId}`, newComment);
      const resComment = res.data.comment;

      const updatedPosts = posts.map(post => {
        if (post.id === postId) {
          return {
            ...post,
            comment: resComment,
          };
        }
        return post;
      });

      setPosts(updatedPosts);
      setCommentInputs(prevInputs => ({ ...prevInputs, [postId]: '' }));
    } catch (error) {
      console.error('Error submitting comment:', error);
    }
  };

  const handleInputChange = (postId, value) => {
    setCommentInputs(prevInputs => ({ ...prevInputs, [postId]: value }));
  };

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString('en-GB', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
    });
  };

  return (
    <div className={`${classes.post} ${className}`}>
      {posts.length > 0 ? (
        posts.map((post) => (
          <PostItem
            key={post.id}
            post={post}
            formatDateTime={formatDateTime}
            handleCommentSubmit={handleCommentSubmit}
            commentInput={commentInputs[post.id]}
            onCommentInputChange={(value) => handleInputChange(post.id, value)}
          />
        ))
      ) : (
        <div>No posts available.</div>
      )}
    </div>
  );
};

export default Post;
