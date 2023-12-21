import { ReactNode, createContext, useReducer, useCallback } from "react";
import { addRecordAPI, deleteRecordAPI } from './ApiServices';

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
  dispatchRecordsList: React.Dispatch<Action>;
  addRecord: (todoName: string) => void;
  deleteRecord: (id: string, todoName: string) => void;
}>({
  recordsList: [],
  dispatchRecordsList: () => {}, // empty function as a placeholder
  addRecord: () => { },
  deleteRecord: () => { }
});
const DataContextProvider = ({ children }: { children: ReactNode; }) => {
  /** RECORD **/
  const [recordsList, dispatchRecordsList] = useReducer(recordsListReducer, []);
  // add record
  const addRecord = useCallback((todoName: string) => {
    addRecordAPI(todoName, dispatchRecordsList);
  }, [dispatchRecordsList]);

  // delete record
  const deleteRecord = useCallback((id: string, todoName: string) => {
    deleteRecordAPI(id, todoName, dispatchRecordsList);
  }, [dispatchRecordsList]);  


  return (
    <DataContext.Provider value={{
      recordsList,
      dispatchRecordsList,
      addRecord,
      deleteRecord
    }}>
      {children}
    </DataContext.Provider>
  );
}
export default DataContextProvider;