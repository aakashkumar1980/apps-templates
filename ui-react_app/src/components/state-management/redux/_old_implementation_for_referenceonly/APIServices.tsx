import { Record } from '../../DataModel';
import recordsListStore from '../Store';
import { deleteRecordAction, getRecordsAction } from '../Actions';

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
        const recordsList = data.map((item: any) => ({
          id: item.id,
          todoName: item.description,
          todoDate: getRandomDate()
        }));
        recordsListStore.dispatch(getRecordsAction(recordsList));
      }
    })
    .catch(error => console.error('Error fetching data:', error));
};

export const deleteRecordAPI = (id: string) => {
  fetch(`http://localhost:8083/api/todos/${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    }
  })
    .then(response => response.json())
    .then(() => {
      const deletedRecord: Record = { id: id, todoName: '', todoDate: '' };
      recordsListStore.dispatch(deleteRecordAction(deletedRecord));
    })

    .catch(error => console.error('Error adding record:', error));
};

