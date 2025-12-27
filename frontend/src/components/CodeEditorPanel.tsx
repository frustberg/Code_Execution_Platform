import Editor from '@monaco-editor/react'
import { useState } from 'react'

type Props = {
  initialCode: string
  language: string
  onSubmit: (code: string) => Promise<void>
}

const languages = ['python', 'javascript', 'java']

export default function CodeEditorPanel({ initialCode, language, onSubmit }: Props) {
  const [activeLang, setActiveLang] = useState(language)
  const [code, setCode] = useState(initialCode)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = async () => {
    setIsSubmitting(true)
    setError(null)
    try {
      await onSubmit(code)
    } catch (e: any) {
      setError(e?.message || 'Submission failed')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="flex flex-col h-full rounded-xl border border-gray-800 bg-surfaceAlt overflow-hidden">
      <div className="flex items-center justify-between px-4 py-2 border-b border-gray-800 bg-surface">
        <div className="flex gap-1">
          {languages.map((lang) => (
            <button
              key={lang}
              className={`px-2.5 py-1 text-xs rounded-full border text-muted hover:text-white hover:border-accent/70 ${
                activeLang === lang ? 'bg-accent text-white border-accent' : 'border-gray-700'
              }`}
              onClick={() => setActiveLang(lang)}
            >
              {lang}
            </button>
          ))}
        </div>
        <div className="flex gap-2">
          <button
            onClick={handleSubmit}
            disabled={isSubmitting}
            className="px-3 py-1.5 rounded-lg bg-accent hover:bg-accentSoft disabled:opacity-60 text-xs font-medium"
          >
            {isSubmitting ? 'Running…' : 'Run & Submit'}
          </button>
        </div>
      </div>
      <div className="flex-1 min-h-[260px]">
        <Editor
          height="100%"
          defaultLanguage={activeLang}
          theme="vs-dark"
          value={code}
          onChange={(v) => setCode(v || '')}
          options={{
            fontSize: 13,
            minimap: { enabled: false },
            wordWrap: 'on',
            scrollBeyondLastLine: false,
            automaticLayout: true
          }}
        />
      </div>
      {error && (
        <div className="px-4 py-2 text-xs text-danger border-t border-red-500/40 bg-red-500/10">
          {error}
        </div>
      )}
    </div>
  )
}