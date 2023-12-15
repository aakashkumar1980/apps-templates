import styles from './Datarecord.module.scss';
import Container from '../../../../Container';
import { TodoContext } from '../../../../../store/TodoStore';
import { useContext } from 'react';

const DataRecords: React.FC = () => {
  const contextItems = useContext(TodoContext);
  
  return (
    <>
      <div id="datarecords">
        {contextItems.map((item, index) => (
          <Container key={index} className={`${styles.rowdata} ${index%2===0? styles.alternateRow:''}`}>
            <div key={index} className="row">
              {item}
            </div>
          </Container>
        ))}
      </div>

      
    </>
  );
};

export default DataRecords;