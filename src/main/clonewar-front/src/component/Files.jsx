import React from 'react'

const applyUpload = (jar) => {
    uploadJar(jar).then(file => {
        console.log(file);
    })
        .catch(err => {
            console.log(err);
        })
}

const uploadJar = async (jar) => {
    const files = jar.target.files;
    const formData = new FormData();
    formData.append('jar', files[0])
    await fetch('http://localhost:8087/api/artifact/upload', {
        method: 'POST',
        body: formData,
        headers: {
            'Content-Type': 'multipart/form-data',
        }
    }).then(r => {
        console.log('error')
        console.log(r)
    });
}

function Files({filename}){
    return (
        <div className={"file"}>
            <label className={"file-label"}>
                <input className={"file-input"} type={"file"} name={filename}/>
                <span className={"file-cta"}>
            <span className="file-icon">
              <i className="fas fa-upload"></i>
            </span>
            <span className={"file-label"} id={"file-name"}>
              Put here your {filename} file...
            </span>
          </span>
            </label>
        </div>
    )
}

export default Files