import React from 'react'
import './Upload.css'

function Upload(props){

    let jar = null;
    const artifactSave =
        {
            name: '',
            url: '',
            date: Date.now().toString()
        }

    const updateArtifact = (url) => {
        artifactSave.url = url;
    }
    const onPutFiles = (file) => {
        console.log(file.target.files);
        jar = file.target.files[0];
    }
    const upload = () => {
        updateArtifact(jar.name);
        const data = new FormData();
        data.append("jar", jar);
        fetch('http://localhost:8087/api/artifact/upload', {
            method: 'POST',
            body: data,
        }).then(data => {
            data.json().then(r => {
                props.set(r);
            })
        }).catch(r => {
            console.log('error !')
            console.log(r)
        });
    }

    const submit = () => {
        console.log(artifactSave);
        fetch('http://localhost:8087/api/artifact/create', {
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(artifactSave),
        }).then(data => {
            console.log(data);
        }).catch(r => {
            console.log('error !')
            console.error(r)
        });
    }

    return (
        <div className={"Upload"}>

            <div className="form">
                <p><b>Fill this form to add another artifact</b></p>
                <div className={"field"}>
                    <label className={"label"}>Artifact name</label>
                    <input className={"input"} type={"text"} onInput={name => artifactSave.name = name.target.value}/>
                </div>
                <div className={"field"}>
                    <label className={"label"}>Files</label>
                    <div className={"file"}>
                        <label className={"file-label"}>
                            <input className={"file-input"} type={"file"} onChange={onPutFiles} id={"jarFileId"}/>
                            <span className={"file-cta"}>
                            <span className="file-icon">
                              <i className="fas fa-upload"/>
                            </span>
                            <span className={"file-label"} id={"file-name"}>
                              Select your archives...
                            </span>
                          </span>
                        </label>
                    </div>
                </div>
                <div className={"field"}>
                    <button className="button" onClick={upload}>Upload jar</button>
                </div>
                <div className={"field"}>
                    <button className="button" onClick={submit}>Create Artifact</button>
                </div>
            </div>
        </div>
    )
}

export default Upload