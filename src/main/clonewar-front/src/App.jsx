import './App.css'
import "bulma/css/bulma.min.css";
import Artifact from "./component/Artifact/Artifact.jsx";
import Files from "./component/Files.jsx";




function App() {
  return (
    <div className="App">
      <h1 className={"title is-1"}><b>Clonewar</b></h1>
      <p>Clonewar is an web-app that allow you to analyze jarfiles and find its clone among others projects</p>
      <Files filename={"jar"}></Files>
      <Artifact></Artifact>
    </div>
  )
}

export default App
