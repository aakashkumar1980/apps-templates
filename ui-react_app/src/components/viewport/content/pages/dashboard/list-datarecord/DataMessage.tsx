import styles from './Datarecord.module.scss';
import { useContext } from 'react';
import { DataContext } from '../../../../../store/DataStore';

const DataMessage: React.FC = () => {
  const {recordsList} = useContext(DataContext);

  return (
    <div id="datagrid">
      {recordsList.length !== 0 ? "" : <div className="alert alert-info">No TODOs found.</div>}
      {recordsList.length === 0 && <div>Please try again later.</div>}

    </div>
  );
};

export default DataMessage;