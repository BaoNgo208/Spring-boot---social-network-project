import React from 'react';
import Post from '../Post';
import { usePostContext } from './PostContext';
const PostDetailPage = () => {

  const { posts } = usePostContext();



  const newPostList = [{
    ...posts[0],
    content: [posts[0].content[0]]
  }];


  if (!posts) return <div>No post found.</div>;

  return <Post posts={[newPostList[0]]} />;
};

export default PostDetailPage;
