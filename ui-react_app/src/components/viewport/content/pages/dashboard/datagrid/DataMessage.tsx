import { useSelector } from 'react-redux';

const DataMessage: React.FC = () => {
  const recordsList = useSelector((state: any) => state.recordsList.recordsList);

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