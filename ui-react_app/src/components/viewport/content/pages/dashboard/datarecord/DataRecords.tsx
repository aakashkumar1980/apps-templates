import styles from './Datarecord.module.scss';
import DataMessage from "./DataMessage";
import Container from '../../../Container';

interface DataRecordsProps {
  items: React.ReactNode[];
}
const DataRecords: React.FC<DataRecordsProps> = ({ items }) => {
  return (
    <>
      <div id="datarecords">
        {items.map((item, index) => (
          <Container key={index} className={`${styles.rowdata} ${index%2===0? styles.alternateRow:''}`}>
            <div key={index} className="row">
              {item}
            </div>
          </Container>
        ))}
      </div>

      <DataMessage items={items}></DataMessage>
    </>
  );
};

export default DataRecords;