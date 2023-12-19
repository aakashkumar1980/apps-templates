import { ReactNode, createContext, useState, useEffect, useReducer } from "react";
import { getRecordsAPI, addRecordAPI, deleteRecordAPI } from './ApiServices';

/** DATA MODEL */
export interface Record {
  id: string; todoName: string; todoDate: string;
}


/** ****************** */
/** REDUCER (FUNCTION) */
/** ****************** */
type Action =
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

/** **************** */
/** CONTEXT PROVIDER */
/** **************** */
export const DataContext = createContext<{
  // record
  recordsList: Record[];
  addRecord: (todoName: string) => void;
  deleteRecord: (id: string, todoName: string) => void;

  // status
  status: string;
}>({
  recordsList: [],
  addRecord: () => { },
  deleteRecord: () => { },

  status: ""
});

const DataContextProvider = ({ children }: { children: ReactNode; }) => {
  /** RECORD **/
  const [recordsList, dispatchRecordsList] = useReducer(recordsListReducer, []);

  // initial data load
  useEffect(() => {
    getRecordsAPI(dispatchRecordsList);
  }, []);
  // add record
  const addRecord = (todoName: string) => {
    addRecordAPI(todoName, dispatchRecordsList);
  };
  // delete record
  const deleteRecord = (id: string, todoName: string) => {
    deleteRecordAPI(id, todoName, dispatchRecordsList);
  }

  
  /** STATUS **/
  const [status, setStatus] = useState<string>("");

  // update status when recordsList changes
  useEffect(() => {
    setStatus(`${recordsList.length} records found.`);
  }, [recordsList.length]);


  return (
    <DataContext.Provider value={{
      recordsList,
      addRecord,
      deleteRecord,

      status
    }}>
      {children}
    </DataContext.Provider>
  );
}
export default DataContextProvider;