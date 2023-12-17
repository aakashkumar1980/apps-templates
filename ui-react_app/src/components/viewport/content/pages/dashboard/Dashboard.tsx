import styles from './Dashboard.module.scss';
import { useContext } from 'react';
import { DataContext } from '../../../../store/DataStore';
import Container from '../../../Container';
import DataStatus from './list-datarecord/DataStatus';
import DataGrid from "./list-datarecord/DataGrid";
import DataMessage from './list-datarecord/DataMessage';


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
            <DataGrid />
            <DataMessage />
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Dashboard;