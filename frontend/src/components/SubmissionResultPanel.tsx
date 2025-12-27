import { CheckCircle2, XCircle, Timer, FileCode } from 'lucide-react'

type Props = { result: any }

export default function SubmissionResultPanel({ result }: Props) {
  if (!result) {
    return (
      <div className="h-full rounded-xl border border-dashed border-gray-700 bg-surfaceAlt flex flex-col items-center justify-center text-center px-6 py-8 text-muted text-xs">
        <FileCode className="mb-2 text-muted" />
        <p>Run your code to see results and test case feedback here.</p>
      </div>
    )
  }

  const status = result.status || result.result || 'unknown'
  const tests = result.results || result.testResults || []
  const isSuccess = status.toLowerCase().includes('success') || status.toLowerCase().includes('pass')

  return (
    <div className="h-full rounded-xl border border-gray-800 bg-surfaceAlt flex flex-col overflow-hidden">
      <div className="px-4 py-2 border-b border-gray-800 bg-surface flex items-center justify-between">
        <div className="flex items-center gap-2 text-xs">
          {isSuccess ? (
            <CheckCircle2 className="text-success" size={16} />
          ) : (
            <XCircle className="text-danger" size={16} />
          )}
          <span className={isSuccess ? 'text-success' : 'text-danger'}>{status}</span>
        </div>
        {typeof result.totalTimeMs === 'number' && (
          <div className="flex items-center gap-1 text-[11px] text-muted">
            <Timer size={14} />
            <span>{result.totalTimeMs} ms</span>
          </div>
        )}
      </div>
      <div className="flex-1 overflow-auto px-4 py-3 text-xs space-y-2">
        {tests.length > 0 ? (
          tests.map((t: any, i: number) => (
            <div
              key={i}
              className="border border-gray-800 rounded-lg p-2 bg-surface flex flex-col gap-1"
            >
              <div className="flex items-center justify-between">
                <span className="font-medium">Test {i + 1}</span>
                <span
                  className={`px-2 py-0.5 rounded-full text-[10px] border ${
                    t.passed
                      ? 'border-green-500/40 text-green-400 bg-green-500/10'
                      : 'border-red-500/40 text-red-400 bg-red-500/10'
                  }`}
                >
                  {t.passed ? 'Passed' : 'Failed'}
                </span>
              </div>
              {t.timeMs && (
                <div className="flex items-center gap-1 text-muted text-[11px]">
                  <Timer size={12} />
                  <span>{t.timeMs} ms</span>
                </div>
              )}
              {t.input && (
                <div>
                  <div className="text-[10px] text-muted">Input</div>
                  <pre className="bg-surfaceAlt border border-gray-800 rounded p-1 mt-0.5">
                    {t.input}
                  </pre>
                </div>
              )}
              {t.expected && (
                <div>
                  <div className="text-[10px] text-muted">Expected</div>
                  <pre className="bg-surfaceAlt border border-gray-800 rounded p-1 mt-0.5">
                    {t.expected}
                  </pre>
                </div>
              )}
              {t.output && (
                <div>
                  <div className="text-[10px] text-muted">Output</div>
                  <pre className="bg-surfaceAlt border border-gray-800 rounded p-1 mt-0.5">
                    {t.output}
                  </pre>
                </div>
              )}
            </div>
          ))
        ) : (
          <pre className="bg-surface border border-gray-800 rounded-lg p-2 text-[11px] whitespace-pre-wrap">
            {JSON.stringify(result, null, 2)}
          </pre>
        )}
      </div>
    </div>
  )
}