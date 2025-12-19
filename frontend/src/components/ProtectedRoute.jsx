import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ children }) => {
    const { isAuthenticated, token } = useSelector((state) => state.auth);

    // Якщо токена нема — точно не залогінений
    if (!token) {
        return <Navigate to="/login" />;
    }

    // Токен є, але user ще вантажиться — не редіректимо
    return isAuthenticated ? children : null;
};

export default ProtectedRoute;
