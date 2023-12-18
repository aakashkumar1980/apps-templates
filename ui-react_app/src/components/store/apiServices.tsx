function getRandomDate() {
  const start = new Date(2020, 0, 1);
  const end = new Date();
  const randomDate = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
  return `${randomDate.getMonth() + 1}/${randomDate.getDate()}/${randomDate.getFullYear()}`;
}

export const getRecordsAPI = (dispatchRecord: Function) => {
  fetch("https://dummyjson.com/todos/user/1")
    .then(res => res.json())
    .then(data => {
      if (data && data.todos) {
        const transformedData = data.todos.map((item: any) => ({
          id: item.id,
          todoName: item.todo,
          todoDate: getRandomDate()
        }));

        dispatchRecord({
          type: "GET_RECORDS",
          payload: transformedData
        });
      }
    })
    .catch(error => console.error('Error fetching data:', error));
};

export const addRecordAPI = (todoName: string, dispatchRecord: Function) => {
  const newRecord = {
    todo: todoName,
    completed: false,
    userId: 1
  };

  fetch("https://dummyjson.com/todos/add", {
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
        todoName: addedRecord.todo,
        todoDate: getRandomDate()
      };
      // Update your state with the new record
      dispatchRecord({ type: 'ADD_RECORD', payload: [transformedRecord] });
    })
    .catch(error => console.error('Error adding record:', error));
};

export const deleteRecordAPI = (id: string, dispatchRecord: Function) => {
  const newRecord = {
    id: id
  };

  fetch("https://dummyjson.com/todos/1", {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(newRecord)
  })
    .then(response => response.json())
    .then(deletedRecord => {
      // Update your state with the deleted record
      dispatchRecord({ type: 'DELETE_RECORD', payload: { id } });
    })
    .catch(error => console.error('Error adding record:', error));
};
