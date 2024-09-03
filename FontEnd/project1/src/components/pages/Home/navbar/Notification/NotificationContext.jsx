import React, { createContext, useState, useContext, useEffect } from 'react';

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
    const [notifications, setNotifications] = useState([]);
    const [hasNewNotification, setHasNewNotification] = useState(false);

    const addNotification = (notification) => {
        setNotifications((prevNotifications) => [notification, ...prevNotifications]);
        setHasNewNotification(true);
        // console.log('New notification added:', notification);
    };

    // Log notifications whenever they change
    // useEffect(() => {
    //     console.log('All notifications:', notifications); // This will log the updated notifications array
    // }, [notifications]);

    const markNotificationsAsRead = () => {
        setHasNewNotification(false);
    };

    return (
        <NotificationContext.Provider value={{ notifications, addNotification, hasNewNotification, markNotificationsAsRead }}>
            {children}
        </NotificationContext.Provider>
    );
};

export const useNotifications = () => useContext(NotificationContext);
