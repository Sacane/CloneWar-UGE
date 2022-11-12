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
    const putFile = (file) => {
        console.log(file)
        updateFiles(_ => {
            if(file.name.endsWith(".zip")){
                files.src = file
            } else {
                files.jar = file;
            }
        })
    }
    const onPressedButton = () => {
        console.log(files.src);
        console.log(files.jar);
    }
    const onPutFiles = (file) => {
        console.log(file.target.files);
        let f = file.target.files;
        if(f.length === 0 || f.length > 2){
            alert('You have to insert the jar AND the src archives !')
        }
        putFile(f[0]);
        putFile(f[1]);
    }
    return (
        <div className={"Upload"}>

            <div className="form">
                <p><b>Fill this form to add another artifact</b></p>
                <div className={"field"}>
                    <label className={"label"}>Artifact name</label>
                    <input className={"input"} type={"text"} placeholder={"Artifact name..."}/>
                </div>
                <div className={"field"}>
                    <label className={"label"}>Files</label>
                    <div className={"file"}>
                        <label className={"file-label"}>
                            <input className={"file-input"} type={"file"} onChange={onPutFiles} id={"jarFileId"} multiple/>
                            <span className={"file-cta"}>
                            <span className="file-icon">
                              <i className="fas fa-upload"></i>
                            </span>
                            <span className={"file-label"} id={"file-name"}>
                              Select your archives...
                            </span>
                          </span>
                        </label>
                    </div>
                </div>
                <div className={"field"}>
                    <button className="button" onClick={onPressedButton}>Create Artifact</button>
                </div>
            </div>
        </div>
    )
}

export default Upload