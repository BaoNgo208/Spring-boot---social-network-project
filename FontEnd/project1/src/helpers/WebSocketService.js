import SockJS from 'sockjs-client';
import { over } from 'stompjs';

let stompClient = null;
let messageCallback = null;
let selectedFriendId = null;
let notificationCallback = null;
let unreadMessages = {}; 

export const connectWebSocket = () => {
  const socket = new SockJS('http://localhost:8080/ws');
  stompClient = over(socket);
  stompClient.connect({}, onConnected, onError);
};

const onConnected = () => {
  stompClient.subscribe('/topic/chat', (payload) => {
    const message = JSON.parse(payload.body);
    const senderId = message.senderId;
    const receiverId = message.receiverId;

    if (messageCallback) {
      const currentUserId = parseInt(sessionStorage.getItem("userId"));
      const isCurrentUserReceiver = receiverId === currentUserId;
      
      if ((selectedFriendId === null || senderId !== selectedFriendId) && isCurrentUserReceiver) {
        if (!unreadMessages[senderId]) {
          unreadMessages[senderId] = 0;
        }
        unreadMessages[senderId]++;
      }

      const messageObject = {
        message,
        unreadMessages: { ...unreadMessages }
      };
      messageCallback(messageObject);
    }
  });

  

  stompClient.subscribe('/notifications/user',(payload)=> {
      const notification = JSON.parse(payload.body);
      if(notificationCallback) {
         handleNotification(notification)
      }
  })
};

const handleNotification = (notification) => {
  
  // Xác định loại thông báo và xử lý tương ứng
  switch (notification.type) {
    case 'COMMENT':
      notificationCallback({ type: 'COMMENT', data: notification });
      break;
    case 'LIKE':
      notificationCallback({ type: 'LIKE', data: notification });
      break;
    case 'FRIEND_REQUEST':
      notificationCallback({ type: 'FRIEND_REQUEST', data: notification });
      break;
    default:
      console.warn('Unknown notification type:', notification.type);
  }
};

const onError = (error) => {
  console.log('WebSocket error:', error);
};

export const sendMessage = (messageContent, receiverId, senderId) => {
  if (messageContent && stompClient) {
    const chatMessage = {
      senderId: parseInt(senderId),
      content: messageContent,
      receiverId: parseInt(receiverId),
    };
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(chatMessage));
  }
};



export const registerMessageCallback = (callback) => {
  messageCallback = callback;
};

export const registerNotificationCallback = (callback) => {
    notificationCallback = callback;
}

export const setSelectedFriend = (friendId) => {
  selectedFriendId = friendId;
  if (friendId !== null && unreadMessages[friendId] !== undefined) {
    unreadMessages[friendId] = 0; 
  }
};

export const disconnectWebSocket = () => {
  if (stompClient !== null) {
    stompClient.disconnect(() => {
      console.log('WebSocket disconnected');
    });
  }
};
