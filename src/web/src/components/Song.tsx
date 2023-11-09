import { useEffect, useState } from 'react'
import type { Song } from '../util/backend'

import styles from '../styles/Song.module.sass'

type Props = {
  song: Song
}

export function Song(props: Props) {

  const [artists, setArtists] = useState<string>("")

  useEffect(() => {
    const lf = new Intl.ListFormat('nl')
    setArtists(lf.format(props.song.artists.map(a => a.name)))
  }, [props.song])

  return <div className={styles.game + " fade"}>
    <div className={styles.image}>
      <div><img src={props.song.album.images[0].url} alt={props.song.name} /></div>
    </div>

    <div className={styles.title}>
      <h1>{props.song.name}</h1>
    </div>

    <div className={styles.artists}>
      <h2>{artists}</h2>
    </div>
  </div>
}