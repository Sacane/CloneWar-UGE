import React from 'react'

function Artifact({artifact:{id, name, date, url}}){
    return (
        <tr key={id}>
            <td>{name}</td>
            <td>{url}</td>
            <td>{date}</td>
            <td><button className={"button is-rounded"}>detail</button></td>
        </tr>
    )
}

export default Artifact