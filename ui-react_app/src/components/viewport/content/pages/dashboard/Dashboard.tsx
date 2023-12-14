import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataRecords from "./datarecord/DataRecords";
import Container from '../../../Container';
import React, { useState } from 'react';


function Dashboard() {
  const [list, setList] = useState([
    { id: 1, todoName: 'Milk', todoDate: '4/10/2020' },
    { id: 2, todoName: 'Rice', todoDate: '8/10/2020' },
    { id: 3, todoName: 'Chocolate', todoDate: '8/10/2020' }
  ]);


  let [todoName, setTodoName] = React.useState<string>();
  const onDelete = (id: number) => {
    setTodoName(list.filter((item) => item.id === id)[0].todoName + " is deleted");
    setList(
      list.filter((item) => item.id !== id)
    );
  }
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
      <Container className="">
        <div style={{ display: "block", width: "100%" }} className="toast" role="alert" aria-live="assertive" aria-atomic="true">
          <div className="toast-header">
            <strong className="me-auto">TODO App</strong>
          </div>

          <div className="toast-body">
            <div className="badge bg-secondary">INFO: <span>{todoName}</span></div>
            <DataRecords items={renderRecords}></DataRecords>
          </div>
        </div>
      </Container>

    </div>
  );
}

export default Dashboard;