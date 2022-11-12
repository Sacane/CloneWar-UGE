import React, {useEffect, useState} from 'react'
import './Artifact.css'
function Artifact(){

    const [artifacts, setArtifacts] = useState([]);
    useEffect(function() {
        fetch("http://localhost:8087/api/artifacts").then(res => res.json())
            .then(data => setArtifacts(data))
    },[]);

    return (
        <div className={"table-wrapper"}>
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
                {artifacts.map(a =>
                    <tr key={a.id}>
                        <td>{a.name}</td>
                        <td>{a.url}</td>
                        <td>{a.date}</td>
                        <td><button className={"button is-rounded"}>detail</button></td>
                    </tr>
                    )}
                </tbody>
            </table>
        </div>

    )
}

export default Artifact