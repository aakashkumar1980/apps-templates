/** TODO: Implement real REST API endpoints with storage */

function getRandomDate() {
  const start = new Date(2020, 0, 1);
  const end = new Date();
  const randomDate = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
  return `${randomDate.getMonth() + 1}/${randomDate.getDate()}/${randomDate.getFullYear()}`;
}

export const getRecordsAPI = (dispatchRecordsList: Function, signal: AbortSignal) => {
  fetch("http://localhost:8083/api/todos", { signal })
    .then(res => res.json())
    .then(data => {
      if (data) {
        const transformedData = data.map((item: any) => ({
          id: item.id,
          todoName: item.description,
          todoDate: getRandomDate()
        }));

        dispatchRecordsList({
          type: "GET_RECORDS",
          payload: transformedData
        });
      }
    })
    .catch(error => console.error('Error fetching data:', error));
};

export const addRecordAPI = (todoName: string, dispatchRecordsList: Function) => {
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
      const transformedRecord = {
        id: addedRecord.id.toString(),
        todoName: addedRecord.description,
        todoDate: getRandomDate()
      };
      // Update your state with the new record
      dispatchRecordsList({ type: 'ADD_RECORD', payload: [transformedRecord] });
    })
    .catch(error => console.error('Error adding record:', error));
};

export const deleteRecordAPI = (id: string, todoName: string, dispatchRecordsList: Function) => {
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
      const transformedRecord = {
        id: id,
        todoName: todoName,
        todoDate: getRandomDate()
      };
      dispatchRecordsList({ type: 'DELETE_RECORD', payload: [transformedRecord] });
    })
    .catch(error => console.error('Error adding record:', error));
};
