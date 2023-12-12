import "./Dashboard.css";
import Record from "./datarecord/Record";
import DataRecords from "./datarecord/DataRecords";

function Dashboard() {
  let list: JSX.Element[] = []; 
  list.push(<Record key={1} />);

  return (
    <div id="dashboard">

      <div className="toast" style={{display:"block"}} role="alert" aria-live="assertive" aria-atomic="true">
        <div className="toast-header">
          <strong className="me-auto">TODO App</strong>
        </div>

        <div className="toast-body">
            <DataRecords items={list}></DataRecords>
        </div>
      </div>

    </div>
  );
}

export default Dashboard;