import React, {useEffect, useState} from 'react';
import './Detail.css';

function Detail(props) {
    const id = window.location.href.split("/")[4];
    const [mainName, setMainName] = useState([]);
    const [artifacts, setArtifacts] = useState([]);

    useEffect(function() {
        fetch("http://localhost:8087/api/artifact/name/" + id).then(res => res.text()).then(name => setMainName(name));
    });

    const handleUpdate = (a) => {
        let arts = [...liArtifacts];
        arts.push(
            <tr key={a.id}>
                <td>{a.name}</td>
                <td>{a.url}</td>
                <td>{a.date}</td>
                <td><button className={"button is-rounded"}>detail</button></td>
            </tr>
        );
        setLiArtifacts(arts);
    }

    return (
        <div className="Detail">
            <h1 className={"title is-1"}>Detail for artifact: { mainName }</h1>

            <p>This page will show you the score of the selected artifact with all the others artifact that have been added to the CloneWar application.</p>
        </div>
    )
}

export default Detail