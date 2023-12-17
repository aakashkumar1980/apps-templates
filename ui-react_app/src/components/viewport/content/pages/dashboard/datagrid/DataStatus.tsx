import styles from './DataGrid.module.scss';
import { BsInfoSquare } from 'react-icons/bs';

interface DataStatusProps {
  items: React.ReactNode;
}
const DataStatus: React.FC<DataStatusProps> = ({ items }) => {
  return (
    <div id="datarecords-info">
      <div style={{ "fontSize": "large" }} className="badge bg-secondary"><BsInfoSquare /> <span>: {items} </span></div>
    </div>
  );
};

export default DataStatus;