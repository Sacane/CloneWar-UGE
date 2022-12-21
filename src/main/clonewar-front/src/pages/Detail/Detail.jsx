import React, {useEffect, useState} from 'react';
import './Detail.css';

function Detail(props) {
    const id = window.location.href.split("/")[4];
    const [mainName, setMainName] = useState([]);
    const [artifacts, setArtifacts] = useState([]);

    function scoreColor(score) {
        if(score >= 80) {
            return {color: 'red'};
        }
        else if(score >= 65) {
            return {color: 'orange'};
        }
        else if(score >= 50) {
            return {color: 'yellow'};
        }
        return {color: 'green'};
    }

    useEffect(function() {
        fetch("http://localhost:8087/api/artifact/name/" + id).then(res => res.text()).then(name => setMainName(name));

        fetch("http://localhost:8087/api/kaplin/score/" + id).then(res => res.json())
            .then(data => {
                let res = data.map(a =>
                    <tr key = {a.id}>
                        <td>{ a.name }</td>
                        <td style={scoreColor(a.score)}>{ a.score }%</td>
                    </tr>
                )
                setArtifacts(res);
            });
    }, []);

    return (
        <div className="Detail">
            <h1 className={"title is-1"}>Detail for artifact: { mainName }</h1>

            <p>This page will show you the score of the selected artifact with all the others artifact that have been added to the CloneWar application.</p>

            <div className={"table-wrapper"}>
                <p><b>Here is the list of the registered, click on the desired one to see its detail</b></p>
                <p><b>There is three differents cases for the score:</b></p>
                <p><b>Between 0-50 it's not a clone</b></p>
                <p><b>Between 50-80 it can be a clone</b></p>
                <p><b>Between 80-100 it's a clone</b></p>
                <table id={"artifacts"} className={"table is-fullwidth"}>
                    <thead>
                        <tr>
                            <th>Artifact name</th>
                            <th>Score</th>
                        </tr>
                        </thead>
                        <tbody>
                            { artifacts }
                        </tbody>
                </table>
            </div>
        </div>
    )
}

export default Detail