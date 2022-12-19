import React, {useState} from 'react'
import './Upload.css'

function Upload(props){

    const [srcFile, setSrcFile] = useState({name: ''});
    const [mainFile, setMainFile] = useState({name: ''});
    const onPutSrc = (file) => {
        setSrcFile(file.target.files[0]);

    }
    const onPutMain = (file) => {
        setMainFile(file.target.files[0]);
    }
    const upload = () => {
        if(mainFile.name === '' || srcFile.name === ''){
            alert("You have to put both src and main archives !");
            return;
        }
        const data = new FormData();
        data.append("main", mainFile);
        data.append("src", srcFile);
        fetch('http://localhost:8087/api/artifact/upload', {
            method: 'POST',
            body: data,
        }).then(data => {
            if(data.status === 200) {
                data.json().then(r => {
                    props.set(r);
                })
            }
        }).catch(r => {
            console.log('TODO : Resolve error properly');
            console.log(r);
        });
    }

    return (
        <div className={"Upload"}>

            <div className="form">
                <p><b>Select your project archives</b></p>
                <div className={"field"}>
                    <label className={"label"}>Main archive (.class)</label>
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
                            <span className={"file-name"}>{mainFile.name}</span>
                        </label>
                    </div>
                </div>
                <div className={"field"}>
                    <label className={"label"}>Source archive (.java)</label>
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
                            <span className={"file-name"}>{srcFile.name}</span>
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