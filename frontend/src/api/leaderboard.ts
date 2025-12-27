import { api } from './client'

export type LeaderboardRow = {
  userId: string
  score?: number
  totalTimeMs?: number
  rank?: number
  [key: string]: any
}

export async function fetchLeaderboard(competitionId: string): Promise<LeaderboardRow[]> {
  const res = await api.get(`/leaderboard/${competitionId}`, { params: { limit: 100 } })
  return res.data
}