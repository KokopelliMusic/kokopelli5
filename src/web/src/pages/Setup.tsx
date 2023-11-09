import { useEffect, useState } from 'react'
import styles from '../styles/Setup.module.sass'
import { getBackendUrl } from '../util/backend'

export default function Setup() {

  const [players, setPlayers] = useState<string[]>([])
  const [playerInput, setPlayerInput] = useState<string>("")

  useEffect(() => {
    setPlayers(playerInput.split(",").map(x => x.trim()).filter(x => x.length > 0))
  }, [playerInput])

  const start = () => {
    fetch(getBackendUrl("game/start?players=" + players.join(",")),
      {
        credentials: 'include'
      })
      .then(async res => {
        if (res.status !== 200) {
          console.error(await res.text())
          throw new Error("Failed to start game")
        }
      })
      .then(() => {
        window.location.href = "/game"
      })
  }

  return <main className={styles.main}>
    <h1>Wie doen er mee?</h1>

    <h5>Spelers kunnen op elk moment toegevoegd worden.</h5>

    <textarea
      value={playerInput}
      onChange={e => setPlayerInput(e.target.value)}
      placeholder="Voer hier de namen in van de mensen die meedoen, gescheiden door een komma." />

    <button onClick={start}>Start spel</button>

    {players.map((player, idx) => <h4 key={player + '-' + idx}>{player}</h4>)}
  </main>
}