import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from "react";
import classes from './Notification.module.css';
import profileUserImg from '../../../../../assests/woman.jpg';
import { useNotifications } from "./NotificationContext";

const Notification = () => {
    const { 
        notifications,
        hasNewNotification,
        markNotificationsAsRead,
        fetchNotifications,
        resetNotifications,
        isLoading
    } = useNotifications(); 

    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate(); 

    const toggleDropdown = () => {
        if (!isOpen) {
            resetNotifications(); // Clear notifications before fetching to avoid duplicates
            fetchNotifications(true); // Fetch initial notifications on first open
            markNotificationsAsRead(); // Mark notifications as read immediately on first open
        }
        setIsOpen(!isOpen);
    };

    useEffect(() => {
        if (isOpen && notifications.length === 0) {
            fetchNotifications(); // Fetch notifications when dropdown is opened and list is empty
        }
    }, [isOpen, notifications.length, fetchNotifications]);

    const handleNotificationClicked = (postId) => {
        navigate(`/postDetail/${postId}`); // Navigate to the post detail page
    };

    const handleScroll = (e) => {
        const bottomReached = e.target.scrollHeight - e.target.scrollTop === e.target.clientHeight;
        if (bottomReached && !isLoading) {
            fetchNotifications(); // Fetch more notifications when scrolled to the bottom
        }
    };

    const getNotificationMessage = (notification) => {
        switch (notification.type) {
            case 'COMMENT':
                return `${notification.senderName} commented on your post`;
            case 'LIKE':
                return `${notification.senderName} liked your post`;
            default:
                return 'New notification';
        }
    };

    return (
        <div className={classes.dropdown}>
            <button onClick={toggleDropdown} className={classes.dropdownToggle}>
                Notifications
                {hasNewNotification && <span className={classes.notificationDot}></span>} 
            </button>
            {isOpen && (
                <div className={classes.dropdownMenu} onScroll={handleScroll}>
                    {notifications.length > 0 ? notifications.map((notification, index) => (
                        <div className={classes.container} key={index} onClick={() => handleNotificationClicked(notification.postId)}>
                            <div className={classes.dropdownItem}>
                                <div>
                                    <img src={profileUserImg} className={classes.profileUserImg} alt="Profile" />
                                </div>
                                <div className={classes.userInfo}>
                                    <div className={classes.buttons}>
                                        {getNotificationMessage(notification)}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )) : <div className={classes.noNotifications}>No notifications</div>}

                    {isLoading && <div>Loading...</div>} {/* Display while fetching more */}
                </div>
            )}
        </div>
    );
};

export default Notification;
