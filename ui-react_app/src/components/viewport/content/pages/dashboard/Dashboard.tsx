import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataRecords from "./datarecord/DataRecords";
import Container from '../../../Container';
import React, { useState } from 'react';


function Dashboard() {
  const [list, setList] = useState([
    <Record recordKey={1} todoName={'Milk'} todoDate={'4/10/2020'} onDelete={() => handleDelete(1)} />,
    <Record recordKey={2} todoName={'Rice'} todoDate={'8/10/2020'} onDelete={() => handleDelete(2)} />,
    <Record recordKey={3} todoName={'Chocolate'} todoDate={'8/10/2020'} onDelete={() => handleDelete(3)} />
  ]);
  let [todoName, setTodoName] = React.useState<string>();
  const handleDelete = (recordKey: number) => {
    const recordToDelete = list.find(record => record.props.recordKey === recordKey);

    if (recordToDelete) 
      setTodoName(recordToDelete.props.todoName+ " deleted.");
    setList(list.filter((record) => {
      return record.props.recordKey !== recordKey;
    }));
  }

  return (
    <div id="dashboard">
      <Container className="">
        <div style={{ display: "block", width: "100%" }} className="toast" role="alert" aria-live="assertive" aria-atomic="true">
          <div className="toast-header">
            <strong className="me-auto">TODO App</strong>
          </div>

          <div className="toast-body">
            <div className="badge bg-secondary">INFO: <span>{todoName}</span></div>
            <DataRecords items={list}></DataRecords>
          </div>
        </div>
      </Container>

    </div>
  );
}

export default Dashboard;