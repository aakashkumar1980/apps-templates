import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataStatus from './datarecord/DataStatus';
import DataRecords from "./datarecord/DataRecords";
import DataMessage from './datarecord/DataMessage';
import Container from '../../../Container';
import React, { useEffect, useState } from 'react';
import { DataContext } from '../../../../store/DataStore';


function Dashboard() {
  /** APPLICATION DATASET */
  interface Todo {
    id: number; todoName: string; todoDate: string;
  }
  const [list, setList] = useState<Todo[]>([]);
  const [status, setStatus] = useState<string>("");
  useEffect(() => {
    const initialData: Todo[] = [
      { id: Math.random(), todoName: 'Milk', todoDate: '4/10/2020' },
      { id: Math.random(), todoName: 'Rice', todoDate: '8/10/2020' },
      { id: Math.random(), todoName: 'Chocolate', todoDate: '8/10/2020' }
    ];
    setList(initialData);
    setStatus(initialData.length+" records found.");
  }, []);



  /** Delete Function */
  const deleteRecord = (id: number) => {
    setStatus(() => {
      let newList = list.filter((item) => item.id !== id);
      return newList.length+" records found.";
    });

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
            <DataStatus items={status}></DataStatus>
            
            <DataContext.Provider value={{
              list: renderRecords,
              deletez: deleteRecord
            }}>
              <DataRecords />
              <DataMessage />
            </DataContext.Provider>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Dashboard;