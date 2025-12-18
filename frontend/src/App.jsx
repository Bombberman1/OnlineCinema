import { BrowserRouter, Routes, Route, useLocation } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import HomePage from "./pages/HomePage";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./components/AdminRoute";
import AuthInitializer from "./components/AuthInitializer";
import Navbar from "./components/Navbar";
import ProfilePage from "./pages/ProfilePage";
import ToastProvider from "./components/ToastProvider";
import ChangePasswordPage from "./pages/ChangePasswordPage";

const Layout = ({ children }) => {
    const location = useLocation();
    const hideNavbar = location.pathname === "/login" || location.pathname === "/signup";

    return (
        <>
            {!hideNavbar && <Navbar />}
            {children}
        </>
    );
};

function App() {
    return (
        <BrowserRouter>
			<ToastProvider>
				<AuthInitializer>
					<Layout>
						<Routes>
							<Route path="/login" element={<LoginPage />} />
							<Route path="/signup" element={<SignupPage />} />
							<Route
								path="/change_password"
								element={
									<ProtectedRoute>
										<ChangePasswordPage />
									</ProtectedRoute>
								}
							/>
							<Route
								path="/profile"
								element={
									<ProtectedRoute>
										<ProfilePage />
									</ProtectedRoute>
								}
							/>


							<Route
								path="/"
								element={
									<ProtectedRoute>
										<HomePage />
									</ProtectedRoute>
								}
							/>

							<Route
								path="/admin"
								element={
									<AdminRoute>
										<div>Admin Dashboard</div>
									</AdminRoute>
								}
							/>
						</Routes>
					</Layout>
				</AuthInitializer>
			</ToastProvider>
        </BrowserRouter>
    );
}

export default App;
