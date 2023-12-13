import styles from './Datarecord.module.scss';

interface DataRecordsProps {
  items: React.ReactNode[];
}
const DataRecords: React.FC<DataRecordsProps> = ({ items }) => {
  return (
    <div id="recorddata-message">
      {items.length !== 0 ? "" : <div className="alert alert-info">No TODOs found.</div>}
      {items.length === 0 && <div>Please try again later.</div>}

    </div>
  );
};

export default DataRecords;