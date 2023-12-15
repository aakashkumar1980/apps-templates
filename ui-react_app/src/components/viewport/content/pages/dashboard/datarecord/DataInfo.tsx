import styles from './Datarecord.module.scss';
import { BsInfoSquare } from 'react-icons/bs';

interface DataInfoProps {
  items: React.ReactNode[];
}
const DataInfo: React.FC<DataInfoProps> = ({ items }) => {
  return (
    <div id="datarecords-info">
      <div style={{ "fontSize": "large" }} className="badge bg-secondary"><BsInfoSquare /> <span>: {items} {items.length !== 0 && " is deleted."}</span></div>
    </div>
  );
};

export default DataInfo;