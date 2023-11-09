export function formatTime(time: number) {
  const hours = Math.floor(time / 3600)
  const minutes = Math.floor((time % 3600) / 60)
  const seconds = Math.floor(time % 60)

  if (hours > 0) {
    return `${pad(hours)}:${pad(minutes)}`
  } else {
    return `${pad(minutes)}:${pad(seconds)}`
  }
}

function pad(num: number) {
  return ('0' + num).slice(-2)
}