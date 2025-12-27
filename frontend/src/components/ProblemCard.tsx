import { Link } from 'react-router-dom'
import type { Problem } from '../api/problems'

const levelColor: Record<string, string> = {
  easy: 'text-green-400 bg-green-400/10 border-green-500/30',
  medium: 'text-yellow-300 bg-yellow-400/10 border-yellow-500/30',
  hard: 'text-red-400 bg-red-400/10 border-red-500/30'
}

type Props = { problem: Problem }

export default function ProblemCard({ problem }: Props) {
  const id = problem.id ?? (problem as any)._id
  const lvl = (problem.level || 'medium').toLowerCase()
  return (
    <Link
      to={`/problems/${id}`}
      className="group rounded-xl border border-gray-800 bg-surfaceAlt hover:border-accent hover:shadow-lg hover:shadow-accent/20 transition-all p-4 flex flex-col gap-3"
    >
      <div className="flex items-center justify-between gap-3">
        <h3 className="font-semibold text-sm md:text-base group-hover:text-accent">
          {problem.title}
        </h3>
        <span
          className={`px-2 py-0.5 rounded-full text-[11px] border ${levelColor[lvl] || levelColor['medium']}`}
        >
          {problem.level ?? 'Medium'}
        </span>
      </div>
      <p className="text-xs text-muted line-clamp-2">{problem.statement}</p>
      <div className="flex flex-wrap gap-1 mt-auto">
        {problem.tags?.slice(0, 4).map((tag) => (
          <span
            key={tag}
            className="px-2 py-0.5 rounded-full bg-surface text-[10px] text-muted border border-gray-800"
          >
            {tag}
          </span>
        ))}
      </div>
    </Link>
  )
}