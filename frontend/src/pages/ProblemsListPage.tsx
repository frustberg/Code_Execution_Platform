import { useEffect, useState } from 'react'
import Layout from '../components/Layout'
import ProblemCard from '../components/ProblemCard'
import { fetchProblems, type Problem } from '../api/problems'
import { Filter, Search } from 'lucide-react'

const difficulties = ['All', 'Easy', 'Medium', 'Hard']

export default function ProblemsListPage() {
  const [problems, setProblems] = useState<Problem[]>([])
  const [filtered, setFiltered] = useState<Problem[]>([])
  const [loading, setLoading] = useState(true)
  const [difficulty, setDifficulty] = useState('All')
  const [query, setQuery] = useState('')

  useEffect(() => {
    const load = async () => {
      try {
        setLoading(true)
        const data = await fetchProblems(1, 50)
        setProblems(data)
        setFiltered(data)
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [])

  useEffect(() => {
    let list = [...problems]
    if (difficulty !== 'All') {
      list = list.filter((p) => (p.level || '').toLowerCase() === difficulty.toLowerCase())
    }
    if (query.trim()) {
      const q = query.toLowerCase()
      list = list.filter(
        (p) =>
          p.title.toLowerCase().includes(q) ||
          (p.tags || []).some((t) => t.toLowerCase().includes(q))
      )
    }
    setFiltered(list)
  }, [difficulty, query, problems])

  return (
    <Layout>
      <div className="flex flex-col gap-4">
        <section className="grid grid-cols-1 md:grid-cols-3 gap-3">
          <div className="rounded-xl border border-gray-800 bg-surfaceAlt p-4 flex flex-col gap-1">
            <div className="text-xs text-muted">Problems</div>
            <div className="text-xl font-semibold">{problems.length}</div>
            <div className="text-[11px] text-muted">Practice & contest problems</div>
          </div>
          <div className="rounded-xl border border-gray-800 bg-surfaceAlt p-4 flex flex-col gap-1">
            <div className="text-xs text-muted">Live Contests</div>
            <div className="text-xl font-semibold">1</div>
            <div className="text-[11px] text-muted">Demo competition running</div>
          </div>
          <div className="rounded-xl border border-gray-800 bg-surfaceAlt p-4 flex flex-col gap-1">
            <div className="text-xs text-muted">Avg. Eval Time</div>
            <div className="text-xl font-semibold">&lt; 5s</div>
            <div className="text-[11px] text-muted">End-to-end from submit to result</div>
          </div>
        </section>

        <section className="rounded-xl border border-gray-800 bg-surfaceAlt p-4 flex flex-col gap-3">
          <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3">
            <div>
              <h2 className="text-sm font-semibold">Problem Set</h2>
              <p className="text-xs text-muted">
                Filter by difficulty or search by title/tags.
              </p>
            </div>
            <div className="flex flex-col sm:flex-row gap-2 md:gap-3">
              <div className="flex items-center gap-2 px-3 py-1.5 rounded-full bg-surface border border-gray-700 text-xs">
                <Search size={14} className="text-muted" />
                <input
                  value={query}
                  onChange={(e) => setQuery(e.target.value)}
                  placeholder="Search problems..."
                  className="bg-transparent outline-none text-xs flex-1"
                />
              </div>
              <div className="flex items-center gap-1 text-xs">
                <Filter size={14} className="text-muted" />
                {difficulties.map((d) => (
                  <button
                    key={d}
                    onClick={() => setDifficulty(d)}
                    className={`px-2 py-1 rounded-full border text-[11px] ${
                      difficulty === d
                        ? 'bg-accent text-white border-accent'
                        : 'border-gray-700 text-muted hover:border-accent/60 hover:text-white'
                    }`}
                  >
                    {d}
                  </button>
                ))}
              </div>
            </div>
          </div>
          {loading ? (
            <div className="py-10 text-center text-xs text-muted">Loading problems…</div>
          ) : filtered.length === 0 ? (
            <div className="py-10 text-center text-xs text-muted">
              No problems found for this filter.
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-3">
              {filtered.map((p) => (
                <ProblemCard key={p.id ?? (p as any)._id} problem={p} />
              ))}
            </div>
          )}
        </section>
      </div>
    </Layout>
  )
}