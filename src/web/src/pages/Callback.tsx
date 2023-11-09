import { useEffect } from 'react'
import { getBackendUrl } from '../util/backend'

export default function Callback() {

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    const code = params.get('code')

    window.location.href = getBackendUrl(`spotify/login/callback?code=${code}`)
  }, [])

  return <div></div>
}