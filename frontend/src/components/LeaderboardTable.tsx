import type { LeaderboardRow } from '../api/leaderboard'

type Props = { rows: LeaderboardRow[]; loading: boolean }

export default function LeaderboardTable({ rows, loading }: Props) {
  if (loading) {
    return (
      <div className="rounded-xl border border-gray-800 bg-surfaceAlt p-4 text-xs text-muted">
        Loading leaderboard...
      </div>
    )
  }

  if (!rows || rows.length === 0) {
    return (
      <div className="rounded-xl border border-gray-800 bg-surfaceAlt p-4 text-xs text-muted">
        No leaderboard data yet. Solve some problems to appear here!
      </div>
    )
  }

  return (
    <div className="rounded-xl border border-gray-800 bg-surfaceAlt overflow-hidden">
      <table className="w-full text-xs">
        <thead className="bg-surface border-b border-gray-800">
          <tr className="text-[11px] text-muted">
            <th className="px-4 py-2 text-left">Rank</th>
            <th className="px-4 py-2 text-left">User</th>
            <th className="px-4 py-2 text-left">Score</th>
            <th className="px-4 py-2 text-left">Time</th>
          </tr>
        </thead>
        <tbody>
          {rows.map((row, i) => (
            <tr
              key={row.userId + i}
              className="border-b border-gray-900/60 hover:bg-surface transition-colors"
            >
              <td className="px-4 py-2 text-[11px] text-muted">{row.rank ?? i + 1}</td>
              <td className="px-4 py-2 text-[11px]">
                <div className="flex items-center gap-2">
                  <div className="h-6 w-6 rounded-full bg-gradient-to-tr from-accent to-purple-500 flex items-center justify-center text-[10px] font-semibold">
                    {row.userId.slice(0, 2).toUpperCase()}
                  </div>
                  <span>{row.userId}</span>
                </div>
              </td>
              <td className="px-4 py-2 text-[11px]">{row.score ?? '-'}</td>
              <td className="px-4 py-2 text-[11px]">
                {typeof row.totalTimeMs === 'number' ? `${row.totalTimeMs} ms` : '-'}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}