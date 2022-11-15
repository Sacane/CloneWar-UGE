import React, {useEffect, useState} from 'react'
import './Upload.css'

const uploadJar = (jar) => {
    const files = jar.target.files;
    const formData = new FormData();
    if(!files[0].name.endsWith(".jar")){
        console.log(files[0].name)
        alert("Wrong file")
        return
    }
    formData.append('jar', files[0])
    fetch('http://localhost:8087/api/artifact/upload', {
        method: 'POST',
        body: formData
    }).then(r => {
        console.log('error')
        console.log(r)
    });
}



function Upload(){

    const [files, updateFiles] = useState(
        {
            jar: null,
            src: null
        }
    );

    let jar = null;

    let artifactSave =
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
        const formData = new FormData();
        formData.append('document', jar)
        formData.append('date', artifactSave.date)
        formData.append('name', artifactSave.name)
        formData.append('url', jar.name)
        console.log('Artifact name : ' + artifactSave.name);
        updateArtifact(jar.name);
        fetch('http://localhost:8087/api/artifact/upload', {
            method: 'POST',
            body: formData,
        }).then(data => {
            console.log(data);
        }).catch(r => {
            console.log('error !')
            console.log(r)
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
                    <button className="button" onClick={upload}>Create Artifact</button>
                </div>
            </div>
        </div>
    )
}

export default Upload