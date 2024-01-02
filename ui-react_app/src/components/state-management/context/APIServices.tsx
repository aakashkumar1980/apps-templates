import { ActionTypes } from "./Actions";

/** TODO: Implement real REST API endpoints with storage */
export const addRecordAPI = (todoName: string, recordsListDispatcher: Function) => {
  const newRecord = {
    title: todoName,
    description: todoName,
    completed: false
  };

  fetch("http://localhost:8083/api/todos", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(newRecord)
  })
    .then(response => response.json())
    .then(addedRecord => {

      recordsListDispatcher({
        type: ActionTypes.ADD_RECORD,
        payload: [addedRecord]
      });
    })
    .catch(error => console.error('Error adding record:', error));
};
