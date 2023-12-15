import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataInfo from './datarecord/DataInfo';
import DataRecords from "./datarecord/DataRecords";
import DataMessage from './datarecord/DataMessage';
import Container from '../../../Container';
import React, { useState } from 'react';


function Dashboard() {
  /** APPLICATION DATASET */
  const [list, setList] = useState([
    { id: 1, todoName: 'Milk', todoDate: '4/10/2020' },
    { id: 2, todoName: 'Rice', todoDate: '8/10/2020' },
    { id: 3, todoName: 'Chocolate', todoDate: '8/10/2020' }
  ]);


  
  /** Delete Function */
  let [deletedTodoNameArray, setDeletedTodoNameArray] = React.useState<string[]>([]);
  const onDelete = (id: number) => {
    setDeletedTodoNameArray(() => [
      ...deletedTodoNameArray,
      list.filter((item) => item.id === id)[0].todoName + " "
    ]);

    setList(
      list.filter((item) => item.id !== id)
    );
  }

  /** RENDER */
  const renderRecords = list.map(item => (
    <Record
      key={item.id}
      id={item.id}
      todoName={item.todoName}
      todoDate={item.todoDate}
      onDelete={onDelete}
    />
  ));
  return (
    <div id="dashboard">
      <Container>
        <div style={{ display: "block", width: "100%" }} className="toast" role="alert" aria-live="assertive" aria-atomic="true">
          <div className="toast-header">
            <strong className="me-auto">TODO App</strong>
          </div>

          <div className="toast-body">
            <DataInfo items={deletedTodoNameArray}></DataInfo>
            <DataRecords items={renderRecords}></DataRecords>
            <DataMessage items={renderRecords}></DataMessage>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Dashboard;