import { ReactNode, createContext, useState, useEffect, useReducer } from "react";
import { getRecordsAPI, addRecordAPI, deleteRecordAPI } from './apiServices';

/** DATA MODEL */
export interface Record {
  id: string; todoName: string; todoDate: string;
}

/** REDUCER (FUNCTION) */
type Action =
  | { type: 'GET_RECORDS'; payload: Record[] }
  | { type: 'ADD_RECORD'; payload: Record[] }
  | { type: 'DELETE_RECORD'; payload: { id: string } };

export function recordsListReducer(currentRecordsList: Record[], action: Action) {
  switch (action.type) {
    case "GET_RECORDS":
      return [...action.payload];
    case "ADD_RECORD":
      // append the new record to the existing records
      return [...currentRecordsList, ...action.payload];
    case "DELETE_RECORD":
      return currentRecordsList.filter(item => item.id !== action.payload.id);
    default:
      return currentRecordsList;
  }
}

/** CONTEXT PROVIDER */
export const DataContext = createContext<{
  // record
  recordsList: Record[];
  addRecord: (todoName: string) => void;
  deleteRecord: (id: string) => void;

  // status
  status: string;
}>({
  recordsList: [],
  addRecord: () => { },
  deleteRecord: () => { },

  status: ""
});

const DataContextProvider = ({ children }: { children: ReactNode; }) => {
  /** record **/
  const [recordsList, dispatchRecordsList] = useReducer(recordsListReducer, []);
  useEffect(() => {
    // initial data load
    getRecordsAPI(dispatchRecordsList);
  }, []);

  const addRecord = (todoName: string) => {
    addRecordAPI(todoName, dispatchRecordsList);
  };
  const deleteRecord = (id: string) => {
    deleteRecordAPI(id, dispatchRecordsList);
  }

  /** status **/
  const [status, setStatus] = useState<string>("");
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