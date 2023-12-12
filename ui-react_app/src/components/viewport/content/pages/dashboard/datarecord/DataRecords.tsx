import styles from '../Dashboard.module.scss';
import DataMessage from "./DataMessage";

interface DataRecordsProps {
  items: React.ReactNode[];
}
const DataRecords: React.FC<DataRecordsProps> = ({ items }) => {
  return (
    <>
      <div id="recorddata-items">
        {items.map((item, index) => (
          <div key={index} className={`${styles.rowdata} row ${index % 2 === 0 ? styles.alternateRow : ''}`}>
            <div>{item}</div>
          </div>
        ))}
      </div>

      <DataMessage items={items}></DataMessage>  
    </>  
  );
};

export default DataRecords;