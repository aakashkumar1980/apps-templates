import styles from './Datarecord.module.scss';
import { useContext } from 'react';
import { DataContext } from '../../../../../store/DataStore';

const DataMessage: React.FC = () => {
  const {list} = useContext(DataContext);

  return (
    <div id="datarecords-message">
      {list.length !== 0 ? "" : <div className="alert alert-info">No TODOs found.</div>}
      {list.length === 0 && <div>Please try again later.</div>}

    </div>
  );
};

export default DataMessage;