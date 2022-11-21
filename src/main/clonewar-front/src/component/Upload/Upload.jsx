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

    return (
        <div className={"Upload"}>

            <div className="form">
                <p><b>Select your jar's source and main archive to create an artifact</b></p>
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
                    <button className="button" onClick={upload}>Create an artifact</button>
                </div>
            </div>
        </div>
    )
}

export default Upload