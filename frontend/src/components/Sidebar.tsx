import { NavLink } from 'react-router-dom'
import { Code2, ListChecks, Trophy } from 'lucide-react'
import clsx from 'clsx'

const navItems = [
  { to: '/problems', label: 'Problems', icon: ListChecks },
  { to: '/leaderboard/demo', label: 'Leaderboard', icon: Trophy }
]

export default function Sidebar() {
  return (
    <aside className="w-60 bg-surfaceAlt border-r border-gray-800 hidden md:flex flex-col">
      <div className="h-16 flex items-center gap-2 px-5 border-b border-gray-800">
        <Code2 className="text-accent" />
        <div>
          <div className="font-semibold text-sm tracking-wide">CodeSprint</div>
          <div className="text-xs text-muted">Code Execution Platform</div>
        </div>
      </div>
      <nav className="flex-1 py-3">
        {navItems.map((item) => {
          const Icon = item.icon
          return (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.to === '/problems'}
              className={({ isActive }) =>
                clsx(
                  'flex items-center gap-3 px-5 py-2.5 text-sm text-muted hover:text-white hover:bg-surface',
                  isActive && 'bg-surface text-white border-r-2 border-accent'
                )
              }
            >
              <Icon size={18} />
              <span>{item.label}</span>
            </NavLink>
          )
        })}
      </nav>
      <div className="px-5 py-3 border-t border-gray-800 text-xs text-muted">
        Built for contests • v1.0
      </div>
    </aside>
  )
}