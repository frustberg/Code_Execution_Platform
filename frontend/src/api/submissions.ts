import { api } from './client'

export type SubmissionResponse = any

export async function submitCode(
  problemId: string,
  language: string,
  code: string,
  competitionId?: string
): Promise<SubmissionResponse> {
  const res = await api.post(
    `/submissions/problem/${problemId}`,
    { language, code, competitionId },
    { headers: { 'X-User-Id': 'demo-user' } }
  )
  return res.data
}