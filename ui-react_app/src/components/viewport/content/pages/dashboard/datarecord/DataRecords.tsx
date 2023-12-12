import styles from '../Dashboard.module.scss';
import DataMessage from "./DataMessage";
import RecordWrapper from "./RecordWrapper";

interface DataRecordsProps {
  items: React.ReactNode[];
}
const DataRecords: React.FC<DataRecordsProps> = ({ items }) => {
  return (
    <>
      <div id="recorddata-items">
        {items.map((item, index) => (
          <RecordWrapper key={index} value={item} />
        ))}
      </div>

      <DataMessage items={items}></DataMessage>  
    </>  
  );
};

export default DataRecords;