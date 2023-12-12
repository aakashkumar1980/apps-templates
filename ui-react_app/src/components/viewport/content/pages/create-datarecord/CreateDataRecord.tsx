import "./CreateDataRecord.module.scss"

function CreateDataRecord() {
  return (
    <div id="create-record-data">
      
      <div style={{display:"block", width:"100%"}} className="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div className="toast-header">
          <strong className="me-auto">TODO App</strong>
        </div>

        <div className="toast-body">
          <div className="row">
            <div className="col-md-6">
              <input type="text" className="form-control" placeholder="Enter TODO here..." />
            </div>
            <div className="col-md-4">
              <input type="date" className="form-control" />
            </div>
            <div className="col-md-2">
              <button className="btn btn-success">Add</button>
            </div>
          </div>
        </div>
      </div>

    </div>
  );
}

export default CreateDataRecord;