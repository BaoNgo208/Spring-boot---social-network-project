import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import classes from "./Notification.module.css";
import profileUserImg from "../../../../../assests/woman.jpg";
import { useNotifications } from "./NotificationContext";
import { faBell } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const Notification = () => {
  const {
    notifications,
    hasNewNotification,
    markNotificationsAsRead,
    fetchNotifications,
    resetNotifications,
    isLoading,
  } = useNotifications();

  const [isOpen, setIsOpen] = useState(false);
  const [updatedNotifications, setUpdatedNotifications] = useState([]);
  const navigate = useNavigate();

  const calculateTimeElapsed = (createdAt) => {
    const now = Date.now();
    const diff = now - createdAt; // Thời gian đã trôi qua (ms)

    const minutes = Math.floor(diff / (1000 * 60));
    if (minutes < 60) return `${minutes} phút trước`;

    const hours = Math.floor(diff / (1000 * 60 * 60));
    if (hours < 24) return `${hours} giờ trước`;

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    if (days < 365) return `${days} ngày trước`;

    const years = Math.floor(days / 365);
    return `${years} năm trước`;
  };

  const toggleDropdown = () => {
    if (!isOpen) {
      resetNotifications(); // Clear notifications before fetching to avoid duplicates
      fetchNotifications(true); // Fetch initial notifications on first open
      markNotificationsAsRead(); // Mark notifications as read immediately on first open
    }
    setIsOpen(!isOpen);
  };

  useEffect(() => {
    if (isOpen && notifications.length > 0) {
      // Cập nhật thời gian cho thông báo khi menu được mở
      const updated = notifications.map((notification) => ({
        ...notification,
        timeElapsed: calculateTimeElapsed(notification.createdAt),
      }));
      setUpdatedNotifications(updated);
    }
  }, [isOpen, notifications]);

  const handleNotificationClicked = (postId) => {
    navigate(`/postDetail/${postId}`); // Navigate to the post detail page
  };

  const handleScroll = (e) => {
    const bottomReached =
      e.target.scrollHeight - e.target.scrollTop === e.target.clientHeight;
    if (bottomReached && !isLoading) {
      fetchNotifications(); // Fetch more notifications when scrolled to the bottom
    }
  };

  const getNotificationMessage = (notification) => {
    switch (notification.type) {
      case "COMMENT":
        return `${notification.senderName} commented on your post`;
      case "LIKE":
        return `${notification.senderName} liked your post`;
      default:
        return "New notification";
    }
  };

  return (
    <div className={classes.dropdown}>
      <button onClick={toggleDropdown} className={classes.dropdownToggle}>
        <FontAwesomeIcon icon={faBell} />
        {hasNewNotification && (
          <span className={classes.notificationDot}></span>
        )}
      </button>
      {isOpen && (
        <div className={classes.dropdownMenu} onScroll={handleScroll}>
          {updatedNotifications.length > 0 ? (
            updatedNotifications.map((notification, index) => (
              <div
                className={classes.container}
                key={index}
                onClick={() => handleNotificationClicked(notification.postId)}
              >
                <div className={classes.dropdownItem}>
                  <div>
                    <img
                      src={profileUserImg}
                      className={classes.profileUserImg}
                      alt="Profile"
                    />
                  </div>
                  <div className={classes.userInfo}>
                    <div className={classes.buttons}>
                      {getNotificationMessage(notification)}
                    </div>
                    <div className={classes.timeElapsed}>
                      <br></br>
                      {notification.timeElapsed}
                    </div>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className={classes.noNotifications}>No notifications</div>
          )}
          {isLoading && <div>Loading...</div>}
        </div>
      )}
    </div>
  );
};

export default Notification;
