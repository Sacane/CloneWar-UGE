import React, {useEffect, useState} from 'react'
import reactLogo from './assets/react.svg'
import './App.css'
import "bulma/css/bulma.min.css";
import Artifact from "./component/Artifact.jsx";

function App() {
  const [artifacts, setArtifacts] = useState([]);
  useEffect(function() {

  //   fetch("http://localhost:8087/api/artifacts").then(res => res.json())
  //       .then(data => setArtifacts(data))
  // }, []);
    fetch("http://localhost:8087/api/artifacts").then(res => res.json())
          .then(data => setArtifacts(data))
    }, []);

  return (
    <div className="App">
      <h1 className={"title is-1"}><b>Clonewar</b></h1>
      <p>Clonewar is an web-app that allow you to analyze jarfiles and find its clone among others projects</p>
      <div className={"table-wrapper"}>
        <p>Here is the list of the registered , click on the desired one to see its detail</p>
        <table id={"artifacts"} className={"table is-fullwidth"}>
          <thead>
          <tr>
            <th>Artifact name</th>
            <th>Url jar</th>
            <th>Input date</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
            {artifacts.map(artifact => <Artifact key={artifact.id} artifact={artifact} />)}
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default App
