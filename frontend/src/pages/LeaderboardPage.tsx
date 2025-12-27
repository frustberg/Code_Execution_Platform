import { useParams } from 'react-router-dom'
import { useEffect, useState } from 'react'
import Layout from '../components/Layout'
import { fetchLeaderboard, type LeaderboardRow } from '../api/leaderboard'
import LeaderboardTable from '../components/LeaderboardTable'

export default function LeaderboardPage() {
  const { competitionId } = useParams()
  const [rows, setRows] = useState<LeaderboardRow[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!competitionId) return
    let cancelled = false

    const load = async () => {
      try {
        setLoading(true)
        const data = await fetchLeaderboard(competitionId)
        if (!cancelled) setRows(data)
      } catch (e: any) {
        if (!cancelled) setError(e?.message || 'Failed to load leaderboard')
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    load()
    const id = setInterval(load, 5000)
    return () => {
      cancelled = True
      clearInterval(id)
    }
  }, [competitionId])

  return (
    <Layout>
      <div className="flex flex-col gap-4">
        <section className="flex flex-col md:flex-row md:items-end md:justify-between gap-3">
          <div>
            <h2 className="text-sm font-semibold">Leaderboard</h2>
            <p className="text-xs text-muted">
              Live ranking for competition <span className="text-accent font-mono">{competitionId}</span>
            </p>
          </div>
          <div className="text-xs text-muted bg-surfaceAlt border border-gray-800 rounded-full px-3 py-1 inline-flex items-center gap-1">
            Updates every 5s • <span className="text-accent">Real-time-ish</span>
          </div>
        </section>

        {error && <div className="text-xs text-danger">{error}</div>}
        <LeaderboardTable rows={rows} loading={loading} />
        {import.meta.env.DEV && (
          <details className="mt-3 text-[11px] text-muted bg-surfaceAlt border border-gray-900 rounded-lg p-2">
            <summary className="cursor-pointer">Raw leaderboard payload (dev only)</summary>
            <pre className="mt-1 whitespace-pre-wrap text-[10px]">
              {JSON.stringify(rows, null, 2)}
            </pre>
          </details>
        )}
      </div>
    </Layout>
  )
}