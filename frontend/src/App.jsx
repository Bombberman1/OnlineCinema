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
import AdminDashboard from "./pages/admin/AdminDashboard";
import AdminMoviesPage from "./pages/admin/AdminMoviesPage";
import AdminVideoFilesPage from "./pages/admin/AdminVideoFilesPage";
import WatchPage from "./pages/WatchPage";

const Layout = ({ children }) => {
    const location = useLocation();
    const hideNavbar =
        location.pathname === "/login" ||
        location.pathname === "/signup";

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
                                path="/"
                                element={
                                    <ProtectedRoute>
                                        <HomePage />
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/watch/:movieId"
                                element={
                                    <ProtectedRoute>
                                        <WatchPage />
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
                                path="/change-password"
                                element={
                                    <ProtectedRoute>
                                        <ChangePasswordPage />
                                    </ProtectedRoute>
                                }
                            />

                            <Route
                                path="/admin"
                                element={
                                    <AdminRoute>
                                        <AdminDashboard />
                                    </AdminRoute>
                                }
                            />

                            <Route
                                path="/admin/movies"
                                element={
                                    <AdminRoute>
                                        <AdminMoviesPage />
                                    </AdminRoute>
                                }
                            />

                            <Route
                                path="/admin/video_files"
                                element={
                                    <AdminRoute>
                                        <AdminVideoFilesPage />
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
