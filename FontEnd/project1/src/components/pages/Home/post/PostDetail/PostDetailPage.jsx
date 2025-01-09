import React from "react";
import Post from "../Post";
import { usePostContext } from "./PostContext";
import { useParams } from "react-router-dom";
const PostDetailPage = () => {
  const { posts } = usePostContext();
  const { id } = useParams();
  const post = posts[0].content.find((post) => post.id === parseInt(id, 10));
  console.log("post detail:", posts);

  const newPostList = [
    {
      ...posts[0],
      content: [post],
    },
  ];

  if (!posts) return <div>No post found.</div>;

  return <Post posts={[newPostList[0]]} />;
};

export default PostDetailPage;
