import { useParams } from 'react-router-dom'
import { useEffect, useState } from 'react'
import Layout from '../components/Layout'
import { fetchProblem, type Problem } from '../api/problems'
import { submitCode } from '../api/submissions'
import CodeEditorPanel from '../components/CodeEditorPanel'
import SubmissionResultPanel from '../components/SubmissionResultPanel'

export default function ProblemDetailPage() {
  const { id } = useParams()
  const [problem, setProblem] = useState<Problem | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [result, setResult] = useState<any>(null)

  useEffect(() => {
    const load = async () => {
      if (!id) return
      try {
        setLoading(true)
        setError(null)
        const data = await fetchProblem(id, 'python')
        setProblem(data)
      } catch (e: any) {
        setError(e?.message || 'Failed to load problem')
      } finally {
        setLoading(false)
      }
    }
    load()
  }, [id])

  const handleSubmit = async (code: string) => {
    if (!id) return
    const res = await submitCode(id, 'python', code)
    setResult(res)
  }

  return (
    <Layout>
      {loading ? (
        <div className="text-xs text-muted">Loading problem…</div>
      ) : error ? (
        <div className="text-xs text-danger">{error}</div>
      ) : !problem ? (
        <div className="text-xs text-muted">Problem not found.</div>
      ) : (
        <div className="grid grid-cols-1 xl:grid-cols-2 gap-4 h-[calc(100vh-6rem)] max-h-[800px]">
          {/* Left: Problem description */}
          <section className="flex flex-col rounded-xl border border-gray-800 bg-surfaceAlt overflow-hidden">
            <div className="px-4 py-3 border-b border-gray-800 bg-surface flex items-center justify-between">
              <div>
                <h2 className="text-sm font-semibold">{problem.title}</h2>
                <p className="text-xs text-muted">Problem #{(problem as any).id || (problem as any)._id}</p>
              </div>
              {problem.level && (
                <span className="px-2 py-0.5 rounded-full text-[11px] bg-surface border border-gray-700 text-muted">
                  {problem.level}
                </span>
              )}
            </div>
            <div className="flex-1 overflow-auto px-4 py-3 text-xs space-y-3">
              <p className="whitespace-pre-wrap leading-relaxed">{problem.statement}</p>
              {problem.tags && problem.tags.length > 0 && (
                <div className="pt-2 border-t border-gray-800 flex flex-wrap gap-1">
                  {problem.tags.map((tag) => (
                    <span
                      key={tag}
                      className="px-2 py-0.5 rounded-full bg-surface text-[10px] text-muted border border-gray-800"
                    >
                      {tag}
                    </span>
                  ))}
                </div>
              )}
            </div>
          </section>

          {/* Right: Code editor + result */}
          <section className="flex flex-col gap-3">
            <div className="h-1/2 min-h-[260px]">
              <CodeEditorPanel
                initialCode={problem.codeStubs?.python || ''}
                language="python"
                onSubmit={handleSubmit}
              />
            </div>
            <div className="h-1/2 min-h-[220px]">
              <SubmissionResultPanel result={result} />
            </div>
          </section>
        </div>
      )}
    </Layout>
  )
}