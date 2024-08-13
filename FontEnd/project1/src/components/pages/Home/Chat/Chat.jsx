import React, { useState, useEffect } from 'react';
import { Button, Form, ListGroup, Card } from 'react-bootstrap';
import { sendMessage } from '../../../../helpers/WebSocketService';

const Chat = ({ friend, onClose, userId, messages }) => {
  const [message, setMessage] = useState('');
  const receiverId = friend.id;

  const handleSendMessage = () => {
    if (message.trim()) {
      sendMessage(message, receiverId, userId);
      setMessage('');
    }
  };

  const getSenderName = (msg) => {
    if (msg.senderId === parseInt(userId)) {
      return sessionStorage.getItem("username");
    } else if (msg.senderId === parseInt(receiverId)) {
      return friend.employee.userName;
    }
    return '';
  };

  return (
    <Card style={{ height: '400px' }} className="chat-container bottom-0 end-0 m-3 shadow">
      <Card.Header className="d-flex justify-content-between align-items-center">
        <div>{friend.employee.userName}</div>
        <Button variant="outline-secondary" size="sm" onClick={onClose}>Close</Button>
      </Card.Header>
      <Card.Body className="chat-body overflow-auto">
        <ListGroup variant="flush">
          {messages.map((msg, index) => (
            <ListGroup.Item key={index}>
              <strong>{getSenderName(msg)}:</strong> {msg.content}
            </ListGroup.Item>
          ))}
        </ListGroup>
      </Card.Body>
      <Card.Footer className="d-flex">
        <Form.Control
          type="text"
          placeholder="Type a message"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}
        />
        <Button variant="primary" onClick={handleSendMessage} className="ms-2">
          Send
        </Button>
      </Card.Footer>
    </Card>
  );
};

export default Chat;
