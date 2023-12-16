import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataStatus from './datarecord/DataStatus';
import DataRecords from "./datarecord/DataRecords";
import DataMessage from './datarecord/DataMessage';
import Container from '../../../Container';
import React, { useEffect, useState, useReducer } from 'react';
import { DataContext } from '../../../../store/DataStore';


interface Todo {
  id: number; todoName: string; todoDate: string;
}
/** REDUCER */
type Action = 
  | { type: 'INIT'; payload: Todo[] }
  | { type: 'DELETE'; payload: { id: number } };

function listReducer(currentList: Todo[], action: Action) {
  switch (action.type) {
    case "INIT":
      return action.payload;
    case "DELETE":
      return currentList.filter(item => item.id !== action.payload.id);
    default:
      return currentList;
  }
}

function Dashboard() {
  /** APPLICATION DATASET */
  const [list, dispatchList] = useReducer(listReducer, []);
  useEffect(() => {
    const initialData: Todo[] = [
      { id: Math.random(), todoName: 'Milk', todoDate: '4/10/2020' },
      { id: Math.random(), todoName: 'Rice', todoDate: '8/10/2020' },
      { id: Math.random(), todoName: 'Chocolate', todoDate: '8/10/2020' }
    ];
    dispatchList({ type: "INIT", payload: initialData });
  }, []);


  /** Delete Function */
  const deleteRecord = (id: number) => {
    dispatchList({ 
      type: "DELETE", 
      payload: {
        id
      } 
    });
  }

  /** RENDER */
  const [status, setStatus] = useState<string>("");
  useEffect(() => {
    setStatus(list.length + " records found.");
  }, [list]); 

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