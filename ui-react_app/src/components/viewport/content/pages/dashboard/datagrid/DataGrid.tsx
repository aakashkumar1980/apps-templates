import styles from './DataGrid.module.scss';
import Container from '../../../../Container';
import DataGridRecord from './DataGridRecord';
import React, { useContext, useEffect } from 'react';
import { DataContext } from '../../../../../store/DataStore';
import { getRecordsAPI } from '../../../../../store/ApiServices';

const DataGrid: React.FC = () => {
  const { recordsList, dispatchRecordsList, deleteRecord } = useContext(DataContext);

  /** load records */ 
  useEffect(() => {
    const controller = new AbortController();
    const signal = controller.signal;
    getRecordsAPI(dispatchRecordsList, signal);

    return () => {
      console.log("Dashboard cleanup.");
      controller.abort();
    };
  }, []); 

  return (
    <>
      <div id="datagrid">
        {recordsList.map((item, index) => (
          <Container key={index} className={`${styles.rowdata} ${index % 2 === 0 ? styles.alternateRow : ''}`}>
            <div key={index} className="row">
              <DataGridRecord
                id={item.id}
                todoName={item.todoName}
                todoDate={item.todoDate} />

              <div style={{ textAlign: 'right' }}>
                <button className="btn btn-danger" onClick={() => item.id !== null && deleteRecord(item.id, item.todoName)}>Delete</button>
              </div>
            </div>
          </Container>

        ))}
      </div>
    </>
  );
};

export default DataGrid;