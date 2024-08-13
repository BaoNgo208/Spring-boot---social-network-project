import React from 'react'
import classes from './rightside.module.css'
import man from '../../../../assests/man.jpg'

const Rightside = (props) => {

  
    return (
        <div className={classes.rightcontainer}>
            <div className={classes.wrapper}>
                {props.friends.map(friend => (
                    <div key={friend.id} className={classes.user} onClick={() => props.onFriendClick(friend)}>
                        <img src={man} className={classes.profileUserImg} />
                        <div className={classes.userData}>
                            <span className='name'>{friend.employee.userName}</span> <br />
                            {props.unreadMessages[friend.id] > 0 && (
                        <span> ({props.unreadMessages[friend.id]} new messages)</span>
                    )}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    )
}

export default Rightside
