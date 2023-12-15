import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataInfo from './datarecord/DataInfo';
import DataRecords from "./datarecord/DataRecords";
import DataMessage from './datarecord/DataMessage';
import Container from '../../../Container';
import React, { useEffect, useState } from 'react';
import { TodoContext } from '../../../../store/TodoStore';


function Dashboard() {
  /** APPLICATION DATASET */
  interface Todo {
    id: number; todoName: string; todoDate: string;
  }
  const [list, setList] = useState<Todo[]>([]);
  useEffect(() => {
    const initialData: Todo[] = [
      { id: Math.random(), todoName: 'Milk', todoDate: '4/10/2020' },
      { id: Math.random(), todoName: 'Rice', todoDate: '8/10/2020' },
      { id: Math.random(), todoName: 'Chocolate', todoDate: '8/10/2020' }
    ];
    setList(initialData);
  }, []);



  /** Delete Function */
  let [deletedTodoNameArray, setDeletedTodoNameArray] = React.useState<string[]>([]);
  const deleteRecord = (id: number) => {
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
            <TodoContext.Provider value={{
              list: renderRecords,
              deletez: deleteRecord
            }}>
              <DataRecords />
              <DataMessage />
            </TodoContext.Provider>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Dashboard;