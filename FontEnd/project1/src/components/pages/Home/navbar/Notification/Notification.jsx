import { useState } from "react";
import classes from './Notification.module.css';
import profileUserImg from '../../../../../assests/woman.jpg';
import { useNotifications } from "./NotificationContext";

const Notification = () => {
    const { notifications, hasNewNotification, markNotificationsAsRead } = useNotifications(); // Sử dụng context
    const [isOpen, setIsOpen] = useState(false);

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
        if (!isOpen) {
            markNotificationsAsRead(); // Đánh dấu là đã đọc khi mở dropdown
        }
    };

    return (
        <div className={classes.dropdown}>
            <button onClick={toggleDropdown} className={classes.dropdownToggle}>
                Thông Báo
                {hasNewNotification && <span className={classes.notificationDot}></span>} {/* Dấu báo đỏ */}
            </button>
            {isOpen && (
                <div className={classes.dropdownMenu}>
                    {notifications.length > 0 ? notifications.map((notification, index) => (
                        <div className={classes.container} key={index}>
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
