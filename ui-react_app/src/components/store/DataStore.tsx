import { ReactNode, createContext, useReducer, useCallback } from "react";
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
  recordsList: Record[];
  recordsListDispatcher: React.Dispatch<Action>;
  
  listRecordsFunction: (signal: AbortController["signal"]) => void;
  addRecordFunction: (todoName: string) => void;
  deleteRecordFunction: (id: string, todoName: string) => void;
}>({
  recordsList: [],
  recordsListDispatcher: () => {},
  
  listRecordsFunction: () => { },
  addRecordFunction: () => { },
  deleteRecordFunction: () => { }
});

const DataContextProvider = ({ children }: { children: ReactNode; }) => {
  /** Define Record */
  const [recordsList, recordsListDispatcher] = useReducer(recordsListReducer, []);

  /** List Records */ 
  const listRecordsFunction = useCallback((signal: AbortController["signal"]) => {  
    getRecordsAPI(recordsListDispatcher, signal);
  }, [recordsListDispatcher]);  
  
  /** Add Record */ 
  const addRecordFunction = useCallback((todoName: string) => {
    addRecordAPI(todoName, recordsListDispatcher);
  }, [recordsListDispatcher]);
  
  /** Delete Record */
  const deleteRecordFunction = useCallback((id: string, todoName: string) => {
    deleteRecordAPI(id, todoName, recordsListDispatcher);
  }, [recordsListDispatcher]);  


  return (
    <DataContext.Provider value={{
      recordsList,
      recordsListDispatcher,

      listRecordsFunction,
      addRecordFunction,
      deleteRecordFunction
    }}>
      {children}
    </DataContext.Provider>
  );
}
export default DataContextProvider;