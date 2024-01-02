import { Actions } from './Actions';
import { recordsListReducer } from './Reducer';
import { ReactNode, createContext, useReducer, useCallback } from "react";
import { addRecordAPI } from './APIServices';


/** **************** */
/** CONTEXT PROVIDER */
/** **************** */
export const DataContext = createContext<{
  recordsListDispatcher: React.Dispatch<Actions>;
  addRecordFunction: (todoName: string) => void;
}>({
  recordsListDispatcher: () => { },
  addRecordFunction: () => { }
});

const DataContextProvider = ({ children }: { children: ReactNode; }) => {
  /** Define Record */
  const [recordsList, recordsListDispatcher] = useReducer(recordsListReducer, []);

  /** Add Record */
  const addRecordFunction = useCallback((todoName: string) => {
    addRecordAPI(todoName, recordsListDispatcher);
  }, [recordsListDispatcher]);

  return (
    <DataContext.Provider value={{
      recordsListDispatcher,
      addRecordFunction
    }}>
      {children}
    </DataContext.Provider>
  );
}
export default DataContextProvider;