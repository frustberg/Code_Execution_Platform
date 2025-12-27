import { api } from './client'

export type Problem = {
  id?: string
  _id?: string
  title: string
  statement: string
  level?: string
  tags?: string[]
  codeStubs?: Record<string, string>
}

export async function fetchProblems(page = 1, limit = 20): Promise<Problem[]> {
  const res = await api.get('/problems', { params: { page, limit } })
  return res.data
}

export async function fetchProblem(id: string, language = 'python'): Promise<Problem> {
  const res = await api.get(`/problems/${id}`, { params: { language } })
  return res.data
}