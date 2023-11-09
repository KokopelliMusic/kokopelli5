import styles from '../styles/Home.module.sass'
import { Link } from 'wouter'

export default function Home() {
  return <main>
    <h1>Welkom bij Kokopelli</h1>

    <div className={styles.line}>
      <h4>Welke versie wil je spelen?</h4>
      <p>Weet je het niet? Kies dan versie 5</p>


      <div>
        <a href="https://player.kokopellimusic.nl/">
          Kokopelli 4
        </a>
        <Link href="/login">
          Kokopelli 5
        </Link>
      </div>
    </div>
  </main>
}