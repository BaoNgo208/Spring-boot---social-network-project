import React, { useState } from 'react';
import api from '../../../../../helpers/api';
import { useEffect } from 'react';
import profileUserImg from '../../../../../assests/woman.jpg';
import {  AiOutlineCheck, AiOutlineClose } from 'react-icons/ai';
import { useQuery   } from "@tanstack/react-query";
import classes from './FriendRequestsDropdown.module.css'; 


const FriendRequestsDropdown = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [friendRequests, setFriendRequests] = useState([]);


  const { data: addFriendRequests } = useQuery({
    queryKey: "friendRequests",
    queryFn: async () => {
      const response = await api.get("http://localhost:8080/employee/get/friend-requests");
      return response.data;
    }
  });

  useEffect(() => {
    if (addFriendRequests) {
      setFriendRequests(addFriendRequests);
    }
  }, [addFriendRequests]);

  const toggleDropdown = () => {
    setIsOpen(!isOpen);
  };

  const handleAccept = async (id) => {
    try {
      await api.post(`http://localhost:8080/employee/accept-friend/request/${id}`, null);
      setFriendRequests(prevRequests => prevRequests.filter(request => request.id !== id));
    } catch (error) {
      console.error('Error accepting friend request:', error);
    }
  };

  const handleDeleteFriendRequest = async (id) => {
    try {
      await api.delete(`http://localhost:8080/employee/delete/friend-request/${id}`, null);
      setFriendRequests(prevRequests => prevRequests.filter(request => request.id !== id));
    } catch (error) {
      console.error('Error accepting friend request:', error);
    }
  };
  return (
    <div className={classes.dropdown}>
      <button onClick={toggleDropdown} className={classes.dropdownToggle}>
        Friend Requests
      </button>
      {isOpen && (
        <div className={classes.dropdownMenu}>
          {friendRequests.length > 0 ? (
            friendRequests.map((request, index) => (
              <div className={classes.container} key={index}>
                <div className={classes.dropdownItem}>
                  <div>
                    <img src={profileUserImg} className={classes.profileUserImg} alt="Profile" />
                  </div>
                  <div className={classes.userInfo}>
                    {request.employee.userName}
                    <div className={classes.buttons}>
                      <button className={classes.acceptButton} onClick={() => handleAccept(request.id)}>
                        <AiOutlineCheck className={classes.acceptIcon} />
                      </button>
                      <button className={classes.declineButton} onClick={() =>handleDeleteFriendRequest(request.id)}>
                        <AiOutlineClose className={classes.declineIcon} />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <div className={classes.dropdownItem}>No friend requests</div>
          )}
        </div>
      )}
    </div>
  );
};


export default FriendRequestsDropdown;