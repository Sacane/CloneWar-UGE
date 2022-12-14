import React from 'react'
import './Artifact.css'
function Artifact(props){


    return (
        <div className={"table-wrapper"}>
            <p><b>Here is the list of the registered, click on the desired one to see its detail</b></p>
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
                { props.artifacts }
                </tbody>
            </table>
        </div>
    )
}

export default Artifact