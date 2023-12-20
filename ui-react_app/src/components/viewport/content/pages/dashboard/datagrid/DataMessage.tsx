import styles from './DataGrid.module.scss';
import { useContext } from 'react';
import { DataContext } from '../../../../../store/DataStore';

const DataMessage: React.FC = () => {
  const { recordsList } = useContext(DataContext);

  return (
    <div id="datagrid">
      <div style={{textAlign:"center", color:"red"}}>
        {recordsList.length !== 0 ? "" : <span>No data found.</span>}
        {recordsList.length === 0 && <span>&nbsp; Please try again later.</span>}
      </div>
    </div>
  );
};

export default DataMessage;