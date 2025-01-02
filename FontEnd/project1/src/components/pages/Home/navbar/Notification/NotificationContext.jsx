import React, { createContext, useState, useContext, useEffect } from 'react';
import api from '../../../../../helpers/api';

const NotificationContext = createContext();

export const NotificationProvider = ({ children }) => {
    const [notifications, setNotifications] = useState([]);
    const [hasNewNotification, setHasNewNotification] = useState(false);
    const [page, setPage] = useState(0);
    const size = 10;
    const [isLoading, setIsLoading] = useState(false);

    // Function to fetch notifications from the API
    const fetchNotifications = async (isInitialLoad = false) => {
        if (isLoading) return; // Prevent multiple fetches at the same time
        setIsLoading(true);

        try {
            const response = await api.get(`/notification/get/notification?page=${page}&size=${size}`);
            const fetchedNotifications = response.data.content;

            if (fetchedNotifications.length > 0) {
                setNotifications((prevNotifications) => [
                    ...prevNotifications,
                    ...fetchedNotifications,
                ]);
                setPage((prevPage) => prevPage + 1);
            }

            // Only mark new notifications if it's the initial load and there are any
            if (isInitialLoad && fetchedNotifications.length > 0) {
                setHasNewNotification(true);
            }
        } catch (error) {
            console.error('Error fetching notifications:', error);
        } finally {
            setIsLoading(false);
        }
    };

    // Function to reset notifications and pagination
    const resetNotifications = () => {
        setNotifications([]);
        setPage(0);
    };

    // Function to mark notifications as read
    const markNotificationsAsRead = () => {
        setHasNewNotification(false);
    };

    // Function to check for new notifications when the component mounts
    const checkNewNotifications = async () => {
        try {
            const response = await api.get(`/notification/get/notification?page=0&size=1`);
            if (response.data.content.length > 0) {
                setHasNewNotification(true);
            } else {
                setHasNewNotification(false);
            }
        } catch (error) {
            console.error('Error checking new notifications:', error);
        }
    };

    // Automatically check for new notifications when the component mounts
    useEffect(() => {
        checkNewNotifications();
    }, []);

    return (
        <NotificationContext.Provider value={{
            notifications,
            fetchNotifications,
            addNotification: (notification) => {
                setNotifications((prevNotifications) => [notification, ...prevNotifications]);
                setHasNewNotification(true);
            },
            hasNewNotification,
            markNotificationsAsRead,
            resetNotifications,
            isLoading
        }}>
            {children}
        </NotificationContext.Provider>
    );
};

export const useNotifications = () => useContext(NotificationContext);
