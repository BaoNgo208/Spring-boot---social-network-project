import React, { useCallback, useEffect, useState } from "react";
import { SearchedUser } from "./SearchedUser";
import { useLocation } from "react-router-dom";
import "./SearchedUserPage.css";
import Post from "../Home/post/Post";
import { useInfiniteQuery } from "@tanstack/react-query";
import api from "../../../helpers/api";
import Sidebar from "../Home/SideBar/Sidebar";
import { useRecommendUsers } from "../../../helpers/context/RecommendUsersContext";
export const SearchedUserPage = () => {
  const recommendUsers = useRecommendUsers();
  const location = useLocation();
  const [searchQuery, setSearchQuery] = useState(() => {
    return location.state?.query || sessionStorage.getItem("searchQuery") || "";
  });

  const [results, setResults] = useState(() => location.state?.results || []);

  useEffect(() => {
    if (location.state) {
      setSearchQuery(location.state.query || "");
      setResults(location.state.results || []);
    }
  }, [location.state]);

  const [userPosts, setUserPosts] = useState([]);

  useEffect(() => {
    if (searchQuery) {
      sessionStorage.setItem("searchQuery", searchQuery);
    }
  }, [searchQuery]);

  const {
    data: userPage,
    fetchNextPage: fetchUserNextPage,
    hasNextPage: hasUserNextPage,
    isFetchingNextPage: isFetchingNextUserPage,
    isLoading: isLoadingUser,
    isError: isErrorFetchingUser,
  } = useInfiniteQuery({
    queryKey: ["user", searchQuery],
    queryFn: async ({ pageParam = 0 }) => {
      try {
        const response = await api.get(
          `http://localhost:8080/employee/get/searchResult?userName=${searchQuery}&page=${pageParam}&size=10`
        );
        return response.data;
      } catch (err) {
        if (err.response?.status === 404) {
          return [];
        }
      }
    },
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = allPages.length;
      return lastPage.length === 0 ? undefined : nextPage;
    },
  });

  const {
    data,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
    isLoading,
    isError,
  } = useInfiniteQuery({
    queryKey: ["searchedUserPosts", searchQuery],
    queryFn: async ({ pageParam = 0 }) => {
      try {
        const response = await api.get(
          `http://localhost:8080/post/get/posts?username=${searchQuery}&page=${pageParam}&size=10`
        );
        return response.data;
      } catch (err) {
        if (err.response?.status === 404) {
          console.warn("No posts found for this user.");
          return [];
        }
        throw err;
      }
    },
    getNextPageParam: (lastPage, allPages) => {
      const nextPage = allPages.length;
      return lastPage.length === 0 ? undefined : nextPage;
    },
  });

  const handleScroll = useCallback(() => {
    if (
      window.innerHeight + document.documentElement.scrollTop !==
        document.documentElement.offsetHeight ||
      isFetchingNextPage ||
      !hasNextPage
    ) {
      return;
    }
    fetchNextPage();
  }, [fetchNextPage, isFetchingNextPage, hasNextPage]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [handleScroll]);

  useEffect(() => {
    if (data) {
      setUserPosts(data.pages.flat());
    }
  }, [data]);

  useEffect(() => {
    if (searchQuery) {
      fetchUserNextPage({ pageParam: 0 });
      fetchNextPage({ pageParam: 0 });
    }
  }, [searchQuery, fetchUserNextPage, fetchNextPage]);

  const users = userPage?.pages.flatMap((page) => page.content) || [];
  console.log("searched users:", users);

  if (isLoading) return <div>Loading...</div>;
  if (isError) return <div>Error fetching data</div>;

  return (
    <div className="main-container">
      <div className="right-nav-bar">
        <Sidebar recommendUsers={recommendUsers} />
      </div>

      <div className="main-content">
        <div className="searched-user-page light-theme">
          <div className="search-header">
            <h2>Kết quả tìm kiếm "{searchQuery}"</h2>
          </div>
          <div className="searched-user-list">
            {users.map((result, id) => (
              <div className="user-item" key={id}>
                <SearchedUser result={result} />
              </div>
            ))}
          </div>
        </div>

        <div className="searched-user-posts">
          <Post posts={userPosts} />
        </div>
      </div>
    </div>
  );
};
