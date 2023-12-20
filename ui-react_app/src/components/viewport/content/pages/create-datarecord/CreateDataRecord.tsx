import { DataContext } from "../../../../store/DataStore";
import "./CreateDataRecord.module.scss"
import React, { useContext, useRef } from "react";

function CreateDataRecord() {
  const { addRecord } = useContext(DataContext);
  const todoNameRef = useRef<HTMLInputElement>(null);
  const todoDateRef = useRef<HTMLInputElement>(null);


  return (
    <div id="create-record-data">
      <div style={{ display: "block", width: "100%" }} className="toast" role="alert" aria-live="assertive" aria-atomic="true">

        <div className="toast-header">
          <strong className="me-auto">TODO App</strong>
        </div>

        <div className="toast-body">
          <form className="row" onSubmit={(event: React.FormEvent<HTMLFormElement>)=> {
            event.preventDefault();
            addRecord(todoNameRef.current!.value);
            todoNameRef.current!.value = "";
            todoDateRef.current!.value = "";
          }}>
            <div className="col-md-6">
              <input
                type="text"
                className="form-control"
                ref={todoNameRef} placeholder="Enter TODO here..." />
            </div>
            <div className="col-md-4">
              <input
                type="date"
                className="form-control"
                ref={todoDateRef} />
            </div>
            <div className="col-md-2">
              <button className="btn btn-success">Add</button>
            </div>
          </form>
        </div>
      </div>

    </div>
  );
}

export default CreateDataRecord;