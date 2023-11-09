import { useQuery } from 'react-query'
import styles from '../styles/Game.module.sass'
import { State, getBackendUrl } from '../util/backend'
import { useEffect, useState } from 'react'
import { formatTime } from '../util/time';
import { Song } from '../components/Song';
import { GameComponent } from '../components/GameComponent';

const SYNC_INTERVAL = 10000

export default function Game() {

  const [time, setTime] = useState(0);
  const [state, setState] = useState<State>();
  const [gamePlaying, setGamePlaying] = useState(false);
  const [bgVis, setBgVis] = useState(false);

  const { isLoading, error, data } = useQuery({
    queryKey: 'game/sync',
    retry: false,
    refetchInterval: SYNC_INTERVAL,
    queryFn: () => {
      const res = fetch(getBackendUrl("game/sync"), {
        credentials: 'include'
      }).then(res => res.json())

      return Promise.all([
        res,
        new Promise((resolve) => setTimeout(resolve, 1000))
      ]).then((res) => res[0])
    }
  })

  /**
   * Setup the app
   */
  useEffect(() => {
    const interval = setInterval(() => setTime(t => t + 1), 1000)

    return () => clearInterval(interval)
  }, [])

  // useEffect(() => {
  // }, [time])

  /**
   * Process the synced data to form the state
   */
  useEffect(() => {
    if (!data) return

    let song = data.currentSong
    let game = data.currentGame

    try {
      song = JSON.parse(song)
    } catch (err) {
      console.error(err)
    }

    if (game) {
      try {
        game = JSON.parse(game)
      } catch (err) {
        console.error(err)
      }
    }

    const s = data
    s.currentSong = song
    if (game) {
      setGamePlaying(true)
      s.currentGame = game
    } else {
      setGamePlaying(false)
    }

    setState(s)
  }, [data])

  useEffect(() => {
    if (gamePlaying) {
      // Flash bg
      for (let i = 0; i < 2400; i += 400) {
        setTimeout(() => setBgVis(b => !b), i)
      }
    } else {
      setBgVis(false)
    }
  }, [gamePlaying])

  /**
   * Handle errors
   */
  useEffect(() => {
    console.error(error)
  }, [error])

  if (isLoading) return <main>
    <h1>Kokopelli</h1>
    <h3>Laden...</h3>
  </main>

  return <div className={styles.game}>
    {bgVis ?
      <div className={styles.flashBg} />
      :
      <div className={styles.staticBg} />
    }

    <div className={styles.settings}>
      ⚙
    </div>

    <div className={styles.title}>
      <h1 className="fade fade-out" key={state?.currentGame?.id}>{state?.currentGame?.title ?? 'Kokopelli'}</h1>
    </div>

    <div className={styles.timer}>
      <time>{formatTime(time)}</time>
    </div>

    <div className={styles.innerGame}>
      {gamePlaying && state?.currentGame ?
        <GameComponent game={state.currentGame} />
        :
        <>
          {state && state.currentSong ?
            <Song key={state.currentSong.id} song={state.currentSong} />
            :
            <h1 className={styles.fade}>Begin met iets spelen op Spotify</h1>
          }
        </>
      }
    </div>

  </div>
}