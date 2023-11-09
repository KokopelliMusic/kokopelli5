import ReactDOM from 'react-dom/client'
import { Route } from 'wouter'
import { QueryClient, QueryClientProvider } from 'react-query'
import Home from './pages/Home'
import Login from './pages/Login'
import Setup from './pages/Setup'
import Callback from './pages/Callback'
import Game from './pages/Game'
import FailedLogin from './pages/FailedLogin'

import './index.sass'

const queryClient = new QueryClient()

ReactDOM.createRoot(document.getElementById('root')!).render(
  <QueryClientProvider client={queryClient}>
    <Route path="/"><Home /></Route>
    <Route path="/login"><Login /></Route>
    <Route path="/login/callback"><Callback /></Route>
    <Route path="/login/fail"><FailedLogin /></Route>
    <Route path="/setup"><Setup /></Route>
    <Route path="/game"><Game /></Route>
  </QueryClientProvider>,
)
