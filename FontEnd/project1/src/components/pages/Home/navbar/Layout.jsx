import Navbar from "./navbar";
const Layout = ({ children }) => {
    return (
      <div>
        <Navbar style={{ zIndex: "1", position: "fixed", top: 0, left: 0, width: "100%" }} />
        <div style={{ marginTop: "60px" }}>{children}</div> {/* Điều chỉnh marginTop nếu cần */}
      </div>
    );
  };
  
  export default Layout;
  