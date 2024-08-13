import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

const UserInfo = (props) => (
    <div className='userInfoContainer'>
        <div className="user-info">
            <h1>Ngô Gia Bảo</h1>
            <p>100 bạn bè</p>
        </div>

        <div className="addFriend">
            <button className="btn btn-primary">{props.isFriend == "true" ? "Bạn Bè" : "Kết Bạn"}</button> 
            <button className="btn btn-primary">Nhắn tin</button>
        </div>
    </div>


    
);

export default UserInfo;
