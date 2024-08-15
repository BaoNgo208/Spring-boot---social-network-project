import React from 'react';
import { useLocation } from 'react-router-dom';
import PostItem from '../PostItem';

const PostDetailPage = () => {
    const location = useLocation();
    const { post } = location.state;

    return (
        <div>
            {post ? (
                <PostItem 
                    post={post} 
                    formatDateTime={(dateTime) => new Date(dateTime).toLocaleString()}
                    // Pass down other necessary props
                />
            ) : (
                <div>No post found.</div>
            )}
        </div>
    );
};

export default PostDetailPage;
