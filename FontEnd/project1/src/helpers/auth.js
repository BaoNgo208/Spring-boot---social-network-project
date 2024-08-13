
export const validateToken = async (token) => {

        const response = await fetch('http://localhost:8080/auth/get/refresh-token', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        const data = await response.json();
        return data;

};
