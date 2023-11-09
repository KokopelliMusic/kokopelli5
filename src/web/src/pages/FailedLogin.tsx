import { useEffect, useState } from 'react'

export default function Callback() {

  const [reason, setReason] = useState("Kutzooi")

  useEffect(() => {
    const params = new URLSearchParams(window.location.search)
    setReason(params.get('reason') ?? "Kutzooi")

  }, [])

  return <main>
    <h1>Login mislukt</h1>

    <h4>{reason}</h4>

    <a href="/login">Probeer opnieuw</a>
  </main>
}