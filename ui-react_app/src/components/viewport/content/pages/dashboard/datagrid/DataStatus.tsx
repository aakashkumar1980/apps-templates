import { useEffect, useState } from 'react';
import { BsInfoSquare } from 'react-icons/bs';
import { useSelector } from 'react-redux';

const DataStatus: React.FC = () => {
  const recordsList = useSelector((state: any) => state.recordsList);

  // update status when recordsList changes
  const [status, setStatus] = useState<string>("");
  useEffect(() => {
    setStatus(`${recordsList.length} records found.`);

    return () => {
      console.log("Status cleanup.");
    }
  }, [recordsList.length]);

  return (
    <div id="datarecords-info">
      <div style={{ "fontSize": "large" }} className="badge bg-secondary"><BsInfoSquare /> <span>: {status} </span></div>
    </div>
  );
};

export default DataStatus;