import { BrowserRouter } from "react-router-dom";
import Viewport from "./components/viewport/Viewport";

function App() {

  return (
    <BrowserRouter>
      <div id="app">
        <Viewport />
      </div>
    </BrowserRouter>
  );
}

export default App;
