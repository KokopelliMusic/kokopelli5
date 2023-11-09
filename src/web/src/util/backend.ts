export type State = {
  cookie: string
  players: Player[]
  currentSong: Song | null
  currentGame: Game | null
  createdAt: string
  updatedAt: string
}

export type Game = {
  id: string
  title: string
  description: string
  players: {
    min: number | undefined
    max: number | undefined
  }
  game: {
    title: string
    first_row: string
  }
}

export type Song = {
  album: Album
  artists: Artist[]
  availableMarkets: string[]
  discNumber: number
  durationMs: number
  href: string
  id: string
  isExplicit: boolean
  isPlayable: boolean
  name: string
  popularity: number
  previewUrl: string
  trackNumber: number
  type: string
  uri: string
}

type Album = {
  albumType: string
  images: Image[]
  href: string
  name: string
  id: string
  url: string
}

type Image = {
  height: number
  url: string
  width: number
}

type Artist = {
  href: string
  id: string
  name: string
  type: string
  uri: string
}

export type Player = {
  name: string
  score: number
  active: boolean
}

export function getBackendUrl(path: string) {
  return `${import.meta.env.VITE_BACKEND_URL}/${path}`;
}

