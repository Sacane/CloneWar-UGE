import React from 'react'
import './Upload.css'

function Upload(props){

    let jar = null;
    let src = null;
    const onPutSrc = (file) => {
        src = file.target.files[0];
        console.log(file.target.files[0]);
    }
    const onPutMain = (file) => {
        jar = file.target.files[0];
        console.log(file.target.files[0]);
    }
    const upload = () => {
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
                <p><b>Select your project archives</b></p>
                <div className={"field"}>
                    <label className={"label"}>Source archive (.class)</label>
                    <div className={"file"}>
                        <label className={"file-label"}>
                            <input className={"file-input"} onChange={onPutMain} type={"file"} id={"mainFileId"}/>
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
                    <label className={"label"}>Main archive (.java)</label>
                    <div className={"file"}>
                        <label className={"file-label"}>
                            <input className={"file-input"} type={"file"} onChange={onPutSrc} id={"srcFileId"}/>
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