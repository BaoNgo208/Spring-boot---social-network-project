// NotificationContext.js
import React, { createContext, useState, useContext } from 'react';

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
    const [notifications, setNotifications] = useState([]);
    const [hasNewNotification, setHasNewNotification] = useState(false); 

    const addNotification = (notification) => {
        setNotifications((prevNotifications) => [notification, ...prevNotifications]);
        setHasNewNotification(true); // Cập nhật khi có thông báo mới
    };

    const markNotificationsAsRead = () => {
        setHasNewNotification(false); // Đặt lại khi người dùng đã xem thông báo
    };

    return (
        <NotificationContext.Provider value={{ notifications, addNotification, hasNewNotification, markNotificationsAsRead }}>
            {children}
        </NotificationContext.Provider>
    );
};

export const useNotifications = () => useContext(NotificationContext);
