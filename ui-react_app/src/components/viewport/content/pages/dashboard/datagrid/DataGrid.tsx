import styles from './DataGrid.module.scss';
import Container from '../../../../Container';
import DataGridRecord from './DataGridRecord';
import React, { useEffect } from 'react';

import { useSelector } from 'react-redux';
import { deleteRecordAPI, getRecordsAPI } from '../../../../../state-management/redux/APIServices';


const DataGrid: React.FC = () => {
  const recordsList = useSelector((state: any) => state.recordsList.recordsList);

  useEffect(() => {
    getRecordsAPI();
  }, []);

  const handleDelete = (id: string) => {
    deleteRecordAPI(id);
  };
  
  return (
    <>
      <div id="datagrid">
        {recordsList.map((item: any, index: number) => (
          <Container key={index} className={`${styles.rowdata} ${index % 2 === 0 ? styles.alternateRow : ''}`}>
            <div key={index} className="row">
              <DataGridRecord
                id={item.id}
                todoName={item.todoName}
                todoDate={item.todoDate} />

              <div style={{ textAlign: 'right' }}>
                <button className="btn btn-danger" onClick={() => item.id !== null && handleDelete(item.id)}>Delete</button>
              </div>
            </div>
          </Container>

        ))}
      </div>
    </>
  );
};

export default DataGrid;