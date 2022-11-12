import './App.css'
import "bulma/css/bulma.min.css";
import Artifact from "./component/Artifact/Artifact.jsx";
import Upload from "./component/Upload/Upload.jsx";
import React from "react";




function App() {
  return (
    <div className="App">
      <h1 className={"title is-1"}><b>Clonewar</b></h1>
        <p>Clonewar is an web-app that allow you to analyze jarfiles and find its clone among others projects</p>
        <p>Here is the list of the registered , click on the desired one to see its detail</p>
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
