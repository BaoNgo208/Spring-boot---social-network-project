import React from 'react';
import classes from './navbar.module.css';
import {  Link, useNavigate } from 'react-router-dom'; 
import {  AiOutlineSearch, AiOutlineLogout } from 'react-icons/ai';
import api from '../../../../helpers/api';
import { useState } from 'react';
import { SearchResultsList } from './SearchResultsList';
import FriendRequestsDropdown from './FriendRequestsDropdown/FriendRequestsDropdown';
import Notification from './Notification/Notification';
import axios from 'axios';
const Navbar = () => {
  const navigate = useNavigate(); 
  const [input, setInput] = useState("");
  const [results,setResult] =useState([]);
  const fetchResult=async (value) => {
      if (!value || value.trim() === "") {
        setResult([])
        return ; 
    }

      const response= await api.get("http://localhost:8080/employee/get/friendListAndMutualFriend"); 
      const matchingUsers = response.data.filter(user => 
      user.userInfoDTO.employee.userName.toLowerCase().includes(value.toLowerCase())
  ).map(user => ({ ...user, isFriend: true }));
      setResult(matchingUsers)
      
      const response2= await api.get("http://localhost:8080/employee/get/getRecommendedFriend");
      response2.data.forEach(item => {
        const commonFriendCount = item.mutualFriend;
        console.log("Common friend count:", commonFriendCount);
    });
      const matchingRecommendedFriends = response2.data.filter(user => 
        user.userInfoDTO.employee.userName.toLowerCase().includes(value.toLowerCase())
      ).map(user => ({ ...user, isFriend: false  }));
    
  
      const combinedResults = [...matchingUsers, ...matchingRecommendedFriends];
  
      setResult(combinedResults);
  }
  
  const handelChange =(value)=> {
     setInput(value)
     fetchResult(value)
  }


  const handleLogout = async () => {
   
        await axios.post('http://localhost:8080/logout', {}, {
            headers: {
                // 'Authorization': 'Bearer ' + Cookies.get('cookie'),
                'Authorization': 'Bearer ' + sessionStorage.getItem('cookie')
            }
        })
        .then(()=> {
            // Cookies.remove("accessToken");
            // Cookies.remove("email");
            // Cookies.remove("cookie");

            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("email");
            sessionStorage.removeItem("cookie");
            navigate('/');
        })
          
  };

  const handleProfile = ()=> {
    navigate(`/profile`);
  }

  
  return (
    
    <div className={classes.container} >
      <div className={classes.wrapper}>
        <div className={classes.left}>
          <Link to='/'>SociaPulse</Link>
        </div>
        <div className={classes.center}>
            <input type="text" placeholder='Search user...' value={input} 
            onChange={(e)=> handelChange(e.target.value)}
            />
            <AiOutlineSearch className={classes.searchIcon} onClick={()=> alert("click")} />
          {/* <SearchBar/> */}
          <SearchResultsList style={{  zIndex: 10 }} results={results} />
        </div>
        <div className={classes.right}>
      
          <FriendRequestsDropdown  className={classes.friendrequests} /> 

          <Notification className={classes.notification}  /> 


          <button className={classes.logoutButton} onClick={handleProfile}>
            <AiOutlineLogout className={classes.logoutButtonIcon} />
            Profile
          </button>

          <button className={classes.logoutButton} onClick={handleLogout}>
            <AiOutlineLogout className={classes.logoutButtonIcon} />
            Log Out
          </button>

          
        </div>
      </div>
    </div>
  );
};

export default Navbar;
