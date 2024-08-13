import React, { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { validateToken } from './auth';
import Cookies from 'js-cookie';
const AuthCheck = ({ location }) => {
    const navigate = useNavigate();
    const hasNavigated = useRef(false); 

    useEffect(() => {
        const checkToken = async () => {
            // const token = Cookies.get('cookie');
            const token = sessionStorage.getItem('cookie');

            if (token && !hasNavigated.current) { 
                const isValid = await validateToken(token);
             
                if (!isValid) {
                    navigate(location.pathname);
                } else {
                    navigate('/home');
                }
                hasNavigated.current = true;
            }
        };

        checkToken();
    }, [location, navigate]);

    return null;
};

export default AuthCheck;
