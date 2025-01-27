import React from "react";
import Post from "../Post";
import { usePostContext } from "./PostContext";
import { useParams } from "react-router-dom";
import { useRecommendUsers } from "../../../../../helpers/context/RecommendUsersContext";
import Sidebar from "../../SideBar/Sidebar";

import "./PostDetailPage.css";
const PostDetailPage = () => {
  const recommendUsers = useRecommendUsers();
  const { posts } = usePostContext();
  const { id } = useParams();
  const post = posts[0].content.find((post) => post.id === parseInt(id, 10));

  const newPostList = [
    {
      ...posts[0],
      content: [post],
    },
  ];

  if (!posts) return <div>No post found.</div>;

  return (
    <div className="main-container">
      <div className="left-bar">
        <Sidebar recommendUsers={recommendUsers}></Sidebar>
      </div>
      <Post posts={[newPostList[0]]} className="post" />
      <div className="right-bars"></div>
    </div>
  );
};

export default PostDetailPage;
