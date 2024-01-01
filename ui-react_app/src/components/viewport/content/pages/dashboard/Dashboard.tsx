import styles from './Dashboard.module.scss';
import Container from '../../../Container';
import DataStatus from './datagrid/DataStatus';
import DataGrid from "./datagrid/DataGrid";
import DataMessage from './datagrid/DataMessage';


function Dashboard() {
  /** ****** */
  /** RENDER */
  /** ****** */
  return (
    <div id="dashboard">
      <Container>
        <div style={{ display: "block", width: "100%" }} className="toast" role="alert" aria-live="assertive" aria-atomic="true">
          <div className="toast-header">
            <strong className="me-auto">TODO App</strong>
          </div>

          <div className="toast-body">
            <DataStatus />
            <DataGrid />
            <DataMessage />
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Dashboard;