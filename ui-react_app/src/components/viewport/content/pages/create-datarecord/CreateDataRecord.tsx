import "./CreateDataRecord.module.scss"
import { DataContext } from "../../../../store/DataStore";
import { useContext, useRef } from "react";


function CreateDataRecord() {
  const { addRecordFunction } = useContext(DataContext);
  const formRef = useRef<HTMLFormElement>(null);
  
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (formRef.current) {
      const formData = new FormData(formRef.current);
      const postData = Object.fromEntries(formData.entries()) as { [key: string]: string };
      await addRecordFunction(postData.todoName);
      formRef.current.reset();
    }
  };

  return (
    <div id="create-record-data">
      <div style={{ display: "block", width: "100%" }} className="toast" role="alert" aria-live="assertive" aria-atomic="true">

        <div className="toast-header">
          <strong className="me-auto">TODO App</strong>
        </div>

        <div className="toast-body">
          <form className="row" ref={formRef} onSubmit={handleSubmit}>
            <div className="col-md-6">
              <input
                type="text"
                className="form-control"
                name="todoName" placeholder="Enter TODO here..." />
            </div>
            <div className="col-md-4">
              <input
                type="date"
                className="form-control"
                name="todoDate" />
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