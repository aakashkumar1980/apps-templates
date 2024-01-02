import store from './Store';
import { ActionTypes } from './Actions';

/** TODO: Implement real REST API endpoints with storage */
function getRandomDate() {
  const start = new Date(2020, 0, 1);
  const end = new Date();
  const randomDate = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
  return `${randomDate.getMonth() + 1}/${randomDate.getDate()}/${randomDate.getFullYear()}`;
}

export const getRecordsAPI = () => {
  fetch("http://localhost:8083/api/todos")
    .then(res => res.json())
    .then(data => {
      if (data) {
        const transformedData = data.map((item: any) => ({
          id: item.id,
          todoName: item.description,
          todoDate: getRandomDate()
        }));

        store.dispatch({
          type: ActionTypes.GET_RECORDS,
          payload: transformedData
        });
      }
    })
    .catch(error => console.error('Error fetching data:', error));
};


export const deleteRecordAPI = (id: string, todoName: string) => {
  const deleteRecord = {
    id: id
  };

  fetch(`http://localhost:8083/api/todos/${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(deleteRecord)
  })
    .then(response => response.json())
    .then(deletedRecord => {
      const deletedRecordTransformed = {
        id: id,
        todoName: todoName,
        todoDate: getRandomDate()
      };

      store.dispatch({
        type: ActionTypes.DELETE_RECORD,
        payload: deletedRecordTransformed
      });       
    })
    .catch(error => console.error('Error adding record:', error));
};

