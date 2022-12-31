import './App.css'
import "bulma/css/bulma.min.css";
import Artifact from "./component/Artifact/Artifact.jsx";
import Upload from "./component/Upload/Upload.jsx";
import React, {useEffect, useState} from "react";
import artifact from "./component/Artifact/Artifact.jsx";
import { useNavigate } from 'react-router-dom';


function App() {
    const navigate = useNavigate();
    const [liArtifacts, setLiArtifacts] = useState([]);

    useEffect(function() {
        fetch("http://localhost:8087/api/artifacts").then(res => res.json())
            .then(data => {
                let res = data.map(a =>
                    <tr key={a.id}>
                        <td>{a.name}</td>
                        <td>{a.date}</td>
                        <td>{a.version}</td>
                        <td>{a.url}</td>
                        <td>{a.contributors}</td>
                        <td><button onClick={() => navigate('/detail/' + a.id)} className={"button is-rounded"}>detail</button></td>
                    </tr>
                )
                setLiArtifacts(res);
            })
    },[]);

    const handleUpdate = (a) => {
        let arts = [...liArtifacts];
        arts.push(
            <tr key={a.id}>
                <td>{a.name}</td>
                <td>{a.date}</td>
                <td>{a.version}</td>
                <td>{a.url}</td>
                <td>{a.contributors}</td>
                <td><button onClick={() => navigate('/detail/' + a.id)} className={"button is-rounded"}>detail</button></td>
            </tr>
        );
        setLiArtifacts(arts);
    }


  return (
    <div className="App">
        <h1 className={"title is-1"}><b>Clonewar</b></h1>
        <p>Clonewar is an web-app that allow you to analyze jarfiles and find its clone among others projects</p>
        <div className={"content"}>
            <Artifact artifacts={liArtifacts}></Artifact>
            <div className={"form"}>
                <Upload artifacts={liArtifacts} set={handleUpdate}></Upload>
            </div>
        </div>
    </div>
  )
}

export default App
