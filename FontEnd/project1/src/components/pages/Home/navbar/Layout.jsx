import React from "react";
import Navbar from "./navbar";
import Rightside from "../rightside/Rightside";

const Layout = ({ children }) => {
  return (
    <div>
      <div
        style={{
          zIndex: 1000,
          position: "fixed",
          top: 0,
          left: 0,
          width: "100%",
          backgroundColor: "#fff",
          boxShadow: "0 2px 5px rgba(0, 0, 0, 0.1)",
        }}
      >
        <Navbar />
      </div>

      <div style={{ marginTop: "60px" }}>{children}</div>
    </div>
  );
};

export default Layout;
