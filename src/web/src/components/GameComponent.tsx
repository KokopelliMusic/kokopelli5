import { Game } from '../util/backend'

import '../styles/GameComponent.sass'

type Props = {
  game: Game
}

export function GameComponent(props: Props) {
  return <div className="GameComponent fade">
    <h1>{props.game.game.title}</h1>
    <h2 dangerouslySetInnerHTML={{ __html: props.game.game.first_row }} />
  </div>
}