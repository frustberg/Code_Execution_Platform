import { Bell, User, LogOut } from 'lucide-react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Topbar() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/register');
  };

  return (
    <header className="h-16 bg-surfaceAlt border-b border-gray-800 flex items-center justify-between px-4 md:px-6">
      <div className="flex-1">
        <h1 className="text-base md:text-lg font-semibold tracking-wide text-white">
          Code Execution Platform
        </h1>
        <p className="text-xs text-muted hidden md:block">
          Solve problems, run code safely, compete in real-time.
        </p>
      </div>
      <div className="flex items-center gap-3">
        <button className="relative p-2 rounded-full bg-surface hover:bg-gray-800">
          <Bell size={18} className="text-muted" />
          <span className="absolute -top-0.5 -right-0.5 h-2 w-2 rounded-full bg-accent" />
        </button>
        <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-surface border border-gray-700">
          <div className="h-7 w-7 rounded-full bg-gradient-to-tr from-accent to-purple-500 flex items-center justify-center text-xs font-semibold">
            {user?.username?.substring(0, 2).toUpperCase() || 'U'}
          </div>
          <div className="hidden sm:flex flex-col">
            <span className="text-xs font-medium">{user?.username || 'User'}</span>
            <span className="text-[10px] text-muted">{user?.email || ''}</span>
          </div>
          <User size={16} className="text-muted hidden sm:block" />
        </div>
        <button
          onClick={handleLogout}
          className="flex items-center gap-2 px-3 py-1.5 rounded-lg bg-red-600 hover:bg-red-700 transition-colors"
          title="Logout"
        >
          <LogOut size={16} className="text-white" />
          <span className="text-xs font-medium text-white hidden sm:inline">Logout</span>
        </button>
      </div>
    </header>
  );
}