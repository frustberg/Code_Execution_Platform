import { Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProblemsListPage from './pages/ProblemsListPage';
import ProblemDetailPage from './pages/ProblemDetailPage';
import LeaderboardPage from './pages/LeaderboardPage';

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        {/* Public routes */}
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        
        {/* Protected routes */}
        <Route
          path="/problems"
          element={
            <PrivateRoute>
              <ProblemsListPage />
            </PrivateRoute>
          }
        />
        <Route
          path="/problems/:id"
          element={
            <PrivateRoute>
              <ProblemDetailPage />
            </PrivateRoute>
          }
        />
        <Route
          path="/leaderboard/:competitionId"
          element={
            <PrivateRoute>
              <LeaderboardPage />
            </PrivateRoute>
          }
        />
        
        {/* Default route - redirect to register */}
        <Route path="/" element={<Navigate to="/register" replace />} />
        <Route path="*" element={<Navigate to="/register" replace />} />
      </Routes>
    </AuthProvider>
  );
}