import styles from './Dashboard.module.scss';
import { useContext } from 'react';
import { DataContext } from '../../../../store/DataStore';
import DataStatus from './datarecord/DataStatus';
import DataRecords from "./datarecord/DataRecords";
import DataMessage from './datarecord/DataMessage';
import Container from '../../../Container';


function Dashboard() {
  const { status } = useContext(DataContext);

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
            <DataStatus items={status}></DataStatus>
            <DataRecords />
            <DataMessage />
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Dashboard;