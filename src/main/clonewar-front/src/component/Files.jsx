import React from 'react'

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