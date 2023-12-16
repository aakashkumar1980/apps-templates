import styles from './Datarecord.module.scss';
import Container from '../../../../Container';
import React, { useContext } from 'react';
import { DataContext } from '../../../../../store/DataStore';
import Record from './Record';

const DataRecords: React.FC = () => {
  const { list, deletez } = useContext(DataContext);
  return (
    <>
      <div id="datarecords">
        {list.map((item, index) => (

          <Container key={index} className={`${styles.rowdata} ${index % 2 === 0 ? styles.alternateRow : ''}`}>
            <div key={index} className="row">
              <Record
                id={item.id}
                todoName={item.todoName}
                todoDate={item.todoDate} />

              <div style={{ textAlign: 'right' }}>
                <button className="btn btn-danger" onClick={() => item.id !== null && deletez(item.id)}>Delete</button>
              </div>
            </div>
          </Container>

        ))}
      </div>
    </>
  );
};

export default DataRecords;