import styles from '../styles/Login.module.sass'
import { getBackendUrl } from '../util/backend'


export default function Login() {
  return <main className={styles.main}>
    <h1>Inloggen met Spotify</h1>
    <h5>Om Kokopelli te gebruiken, moet je inloggen met Spotify</h5>

    <a href={getBackendUrl('spotify/login')}>Login</a>
  </main>
}