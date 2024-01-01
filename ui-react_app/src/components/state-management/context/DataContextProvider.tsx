import { Action, recordsListReducer } from './Reducer';
import { ReactNode, createContext, useReducer, useCallback } from "react";
import { Record, getRecordsAPI, addRecordAPI, deleteRecordAPI } from '../APIServices';


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