import React, { ReactNode, createContext, useState, useEffect, useReducer } from "react";
import { _sample_records } from "./sample_records";


/** DATA MODEL */
export interface Record {
  id: string; todoName: string; todoDate: string;
}

/** REDUCER (FUNCTION) */
type Action =
  | { type: 'INIT'; payload: Record[] }
  | { type: 'DELETE'; payload: { id: string } };

export function recordReducer(currentRecords: Record[], action: Action) {
  switch (action.type) {
    case "INIT":
      return action.payload;
    case "DELETE":
      return currentRecords.filter(item => item.id !== action.payload.id);
    default:
      return currentRecords;
  }
}

/** CONTEXT PROVIDER */
export const DataContext = createContext<{
  // record
  recordsList: Record[];
  dispatchRecord: React.Dispatch<Action>;
  deleteRecord: (id: string) => void;

  // status
  status: string;
}>({
  recordsList: [],
  dispatchRecord: () => { },
  deleteRecord: () => { },

  status: ""
});

const DataContextProvider = ({ children }: {
  children: ReactNode;
}) => {
  // record
  const [recordsList, dispatchRecord] = useReducer(recordReducer, _sample_records);
  const deleteRecord = (id: string) => {
    dispatchRecord({
      type: "DELETE",
      payload: { id }
    });
  }

  // status
  const [status, setStatus] = useState<string>("");
  useEffect(() => {
    setStatus(`${recordsList.length} records found.`);
  }, [recordsList.length]);

  return (
    <DataContext.Provider value={{
      recordsList,
      dispatchRecord,
      deleteRecord,

      status
    }}>
      {children}
    </DataContext.Provider>
  );
}
export default DataContextProvider;