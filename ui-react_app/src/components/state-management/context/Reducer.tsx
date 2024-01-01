import { Record } from '../APIServices';


export type Action =
  | { type: 'GET_RECORDS'; payload: Record[] }
  | { type: 'ADD_RECORD'; payload: Record[] }
  | { type: 'DELETE_RECORD'; payload: Record[] };

export function recordsListReducer(currentRecordsList: Record[], action: Action) {
  const newRecordsList = action.payload
  console.log("currentRecordsList: ", currentRecordsList);
  console.log("newRecordsList: ", newRecordsList);

  switch (action.type) {
    case "GET_RECORDS":
      return [...newRecordsList];
    case "ADD_RECORD":
      return [...currentRecordsList, ...newRecordsList];
    case "DELETE_RECORD":
      return currentRecordsList.filter(item => item.id !== newRecordsList[0].id);
    default:
      return currentRecordsList;
  }
}