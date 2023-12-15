import styles from './Datarecord.module.scss';
import Container from '../../../../Container';
import React, { useContext } from 'react';
import { DataContext } from '../../../../../store/DataStore';


const DataRecords: React.FC = () => {
  const { list, deletez } = useContext(DataContext);

  return (
    <>
      <div id="datarecords">
        {list.map((item, index) => (
          (() => {
            const id = React.isValidElement(item) ? item.props.id : null;
            
            return (
              <Container key={index} className={`${styles.rowdata} ${index % 2 === 0 ? styles.alternateRow : ''}`}>
                <div key={index} className="row">
                  {item}
                  <div style={{textAlign:'right'}}>
                    <button className="btn btn-danger" onClick={() => id !== null && deletez(id)}>Delete</button>
                  </div>
                </div>
              </Container>
            );
          })()

        ))}
      </div>


    </>
  );
};

export default DataRecords;