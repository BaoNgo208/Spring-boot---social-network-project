import axios from 'axios';
import {jwtDecode} from 'jwt-decode';
import Cookies from 'js-cookie';

// const token = Cookies.get('accessToken');
const token = sessionStorage.getItem('accessToken');
let isRevoked = false;

const api = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Authorization': 'Bearer ' + token
    }
});

// Hàm kiểm tra token có hết hạn không
export const isTokenExpired = (token) => {
    if (!token) return true; 
    const decoded = jwtDecode(token);
    const expirationDate = new Date(decoded.exp * 1000);
    const currentDate = new Date();
    return expirationDate < currentDate;
};


export const isRefreshTokenRevoked = async () => {
    try {
        const response = await axios.get('http://localhost:8080/auth/get/refresh-token', {
            headers: {
                // 'Authorization': 'Bearer ' + Cookies.get('cookie'),
                'Authorization': 'Bearer ' + sessionStorage.getItem('cookie')
            }
        });
        return response.data;
    } catch (error) {
        console.error('Error checking refresh token status:', error);
        throw error;
    }
};


export const getRefreshToken = async () => {
    try {
        const response = await axios.post('http://localhost:8080/auth/refresh-token', {}, {
            headers: {
                // 'Authorization': 'Bearer ' + Cookies.get('cookie'),
                'Authorization': 'Bearer ' + sessionStorage.getItem('cookie')
            }
        });
        return response.data.access_token;
    } catch (error) {
        console.error('Error refreshing token:', error);
        throw error;
    }
};


export const logout = async () => {
    try {
        await axios.post('http://localhost:8080/logout', {}, {
            headers: {
                // 'Authorization': 'Bearer ' + Cookies.get('cookie'),
                'Authorization': 'Bearer ' + sessionStorage.getItem('cookie')
            }
        });
   
    
    } catch (error) {
        console.error('Error logging out:', error);
        throw error;
    }
};

api.interceptors.request.use(
    async (config) => {
        if (config.url.endsWith('/logout')) {
            return config;
        }

        if (!isRevoked) {
            try {
                isRevoked = await isRefreshTokenRevoked();
                if (isRevoked) {
                    await logout();
                    // Cookies.remove("accessToken");
                    // Cookies.remove("email");
                    // Cookies.remove("cookie");
                    sessionStorage.removeItem("accessToken")
                    sessionStorage.removeItem("email")
                    sessionStorage.removeItem("cookie")
                    window.location.href = '/';
                    
                    return Promise.reject(new Error('Refresh token revoked'));
                }
            } catch (error) {
                console.error('Error checking refresh token:', error);
                return Promise.reject(error);
            }
        }

        if (isTokenExpired(sessionStorage.getItem("cookie")) && sessionStorage.getItem("cookie") != null) {
            await logout();
            alert("Token hết hạn");
            // Cookies.remove("accessToken");
            // Cookies.remove("email");
            // Cookies.remove("cookie");

            sessionStorage.removeItem("accessToken")
            sessionStorage.removeItem("email")
            sessionStorage.removeItem("cookie")
            window.location.href = '/';
            return Promise.reject(new Error('Token expired'));
        }

        if (isTokenExpired(token)) {
            try {
                const refreshedToken = await getRefreshToken();
                // Cookies.set('accessToken', refreshedToken, {expires: 14});
                sessionStorage.setItem('accessToken', refreshedToken);
                config.headers['Authorization'] = `Bearer ${refreshedToken}`;
            } catch (error) {
                console.error('Error refreshing token:', error);
                return Promise.reject(error);
            }
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;
