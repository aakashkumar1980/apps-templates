import styles from './Dashboard.module.scss';
import { useContext, useEffect, useState } from 'react';
import { DataContext } from '../../../../store/DataStore';
import Container from '../../../Container';
import DataStatus from './datagrid/DataStatus';
import DataGrid from "./datagrid/DataGrid";
import DataMessage from './datagrid/DataMessage';
import { getRecordsAPI } from '../../../../store/ApiServices';


function Dashboard() {
  /** RECORD **/
  const { recordsList, dispatchRecordsList } = useContext(DataContext);
  // load records
  useEffect(() => {
    getRecordsAPI(dispatchRecordsList);

    return () => {
      console.log("Dashboard cleanup");
    };
  }, []); 

  /** STATUS **/
  const [status, setStatus] = useState<string>("");
  // update status when recordsList changes
  useEffect(() => {
    setStatus(`${recordsList.length} records found.`);

    return () => {
      console.log("cleanup setStatus");
    }
  }, [recordsList.length]);
  
  
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