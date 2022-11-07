import React, {useEffect, useState} from 'react'
import './App.css'
import "bulma/css/bulma.min.css";
import Artifact from "./component/Artifact.jsx";
import Files from "./component/Files.jsx";

const applyUpload = (jar) => {
  uploadJar(jar).then(file => {
    console.log(file);
  })
      .catch(err => {
        console.log(err);
      })
}

const uploadJar = async (jar) => {
  console.log("UPLOAD")
  const files = jar.target.files;
  const formData = new FormData();
  formData.append('jar', files[0])
  console.log(formData);
  console.log(files[0])
  await fetch('http://localhost:8087/api/artifact/upload', {
    method: 'POST',
    body: formData,
    headers: {
    'Content-Type': 'multipart/form-data',
    }
  }).then(r => {
    console.log('error')
    console.log(r)
  });
}


function App() {
  const [artifacts, setArtifacts] = useState([]);
  useEffect(function() {
    fetch("http://localhost:8087/api/artifacts").then(res => res.json())
          .then(data => setArtifacts(data))
    },[]);

  return (
    <div className="App">
      <h1 className={"title is-1"}><b>Clonewar</b></h1>
      <p>Clonewar is an web-app that allow you to analyze jarfiles and find its clone among others projects</p>
      <Files filename={"jar"}></Files>
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
