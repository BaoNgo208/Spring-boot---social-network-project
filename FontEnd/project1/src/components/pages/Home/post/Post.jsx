import React, { useState, useEffect, useCallback } from 'react';
import Comment from '../../../../entites/Comment';
import api from '../../../../helpers/api';
import PostItem from './PostItem';

const Post = (props) => {
  const [posts, setPosts] = useState([]);
  const [commentInputs, setCommentInputs] = useState({});

  useEffect(() => {
    if (props.posts && props.posts.length > 0) {
      // Create a set of existing post IDs
      const existingPostIds = new Set(posts.map(post => post.id));

      // Filter out posts that already exist in the state
      const newPosts = props.posts.flatMap(page => page.content).filter(post => !existingPostIds.has(post.id));

      // Update posts state with only new posts
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
  }, [props.posts]);

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
    <div>
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
