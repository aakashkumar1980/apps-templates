import styles from './DataGrid.module.scss';
import Container from '../../../../Container';
import React, { useContext } from 'react';
import { DataContext } from '../../../../../store/DataStore';
import DataGridRecord from './DataGridRecord';

const DataGrid: React.FC = () => {
  const { recordsList, deleteRecord } = useContext(DataContext);
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
                <button className="btn btn-danger" onClick={() => item.id !== null && deleteRecord(item.id)}>Delete</button>
              </div>
            </div>
          </Container>

        ))}
      </div>
    </>
  );
};

export default DataGrid;