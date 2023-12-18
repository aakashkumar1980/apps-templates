import React, { ReactNode, createContext, useState, useEffect, useReducer } from "react";
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

export function recordReducer(currentRecords: Record[], action: Action) {
  switch (action.type) {
    case "GET_RECORDS":
      return [...action.payload];   
    case "ADD_RECORD":
      // append the new record to the existing records
      return [...currentRecords, ...action.payload];
    case "DELETE_RECORD":
      return currentRecords.filter(item => item.id !== action.payload.id);
    default:
      return currentRecords;
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

const DataContextProvider = ({ children }: {
  children: ReactNode;
}) => {
  /** record **/
  const [recordsList, dispatchRecord] = useReducer(recordReducer, []);
  useEffect(() => {
    // initial data load
    getRecordsAPI(dispatchRecord);
  }, []);

  const addRecord = (todoName: string) => {
    addRecordAPI(todoName, dispatchRecord);
  };
  const deleteRecord = (id: string) => {
    deleteRecordAPI(id, dispatchRecord);
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