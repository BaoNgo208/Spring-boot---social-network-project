import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import Cookies from 'js-cookie';
const isAuthenticated = () => {
    return Cookies.get('accessToken') !== null;
  };
  

export const ProtectedRoutes = () => {
    return isAuthenticated()  ? <Outlet /> : <Navigate to="/"/>
}