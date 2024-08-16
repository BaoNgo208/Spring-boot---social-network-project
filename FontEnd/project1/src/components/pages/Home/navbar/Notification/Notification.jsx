import { useNavigate } from 'react-router-dom';
import { useState } from "react";
import classes from './Notification.module.css';
import profileUserImg from '../../../../../assests/woman.jpg';
import { useNotifications } from "./NotificationContext";
const Notification = () => {
    const { notifications, hasNewNotification, markNotificationsAsRead } = useNotifications(); 
    const [isOpen, setIsOpen] = useState(false);
    const navigate = useNavigate(); // Sử dụng useNavigate để chuyển hướng
    
    const toggleDropdown = () => {
        setIsOpen(!isOpen);
        if (!isOpen) {
            markNotificationsAsRead(); 
        }
    };

    const handleNotificationClicked = (postId) => {
        navigate(`/postDetail/${postId}`); // Chuyển hướng đến PostDetailPage
        console.log("id:" + postId)
    };

    return (
        <div className={classes.dropdown}>
            <button onClick={toggleDropdown} className={classes.dropdownToggle}>
                Thông Báo
                {hasNewNotification && <span className={classes.notificationDot}></span>} 
            </button>
            {isOpen && (
                <div className={classes.dropdownMenu}>
                    {notifications.length > 0 ? notifications.map((notification, index) => (
                        <div className={classes.container} key={index} onClick={() => handleNotificationClicked(notification.postId)}>
                            <div className={classes.dropdownItem}>
                                <div>
                                    <img src={profileUserImg} className={classes.profileUserImg} alt="Profile" />
                                </div>
                                <div className={classes.userInfo}>
                                    <div className={classes.buttons}>
                                        {notification.message}
                                    </div>
                                </div>
                            </div>
                        </div>
                    )) : <div className={classes.noNotifications}>Không có thông báo</div>}
                </div>
            )}
        </div>
    );
};

export default Notification;