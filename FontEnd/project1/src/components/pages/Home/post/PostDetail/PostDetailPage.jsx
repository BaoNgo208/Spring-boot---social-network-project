import React from 'react';
import Post from '../Post';
import { usePostContext } from './PostContext';
import { useParams } from 'react-router-dom';
const PostDetailPage = () => {

  const { posts } = usePostContext();
  console.log(posts)
  const { id } = useParams(); // Lấy postId từ URL
  const post = posts[0].content.find(post =>  post.id === parseInt(id, 10));

  const newPostList = [{
    ...posts[0],
    content: [post]
  }];


  if (!posts) return <div>No post found.</div>;

  return <Post posts={[newPostList[0]]} />;
};

export default PostDetailPage;
