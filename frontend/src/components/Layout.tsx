import { ReactNode } from 'react'
import Sidebar from './Sidebar'
import Topbar from './Topbar'

type Props = { children: ReactNode }

export default function Layout({ children }: Props) {
  return (
    <div className="min-h-screen flex bg-bg text-white">
      <Sidebar />
      <div className="flex-1 flex flex-col">
        <Topbar />
        <main className="flex-1 overflow-y-auto px-6 py-4 bg-surface">
          {children}
        </main>
      </div>
    </div>
  )
}