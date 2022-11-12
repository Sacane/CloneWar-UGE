import './App.css'
import "bulma/css/bulma.min.css";
import Artifact from "./component/Artifact/Artifact.jsx";
import Upload from "./component/Upload/Upload.jsx";




function App() {
  return (
    <div className="App">
      <h1 className={"title is-1"}><b>Clonewar</b></h1>
      <p>Clonewar is an web-app that allow you to analyze jarfiles and find its clone among others projects</p>
        <div className={"content"}>
            <Artifact></Artifact>
            <div className={"form"}>
                <Upload></Upload>
            </div>
        </div>
    </div>
  )
}

export default App
