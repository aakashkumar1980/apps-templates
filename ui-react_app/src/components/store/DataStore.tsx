import React, { ReactNode, createContext, useState, useEffect, useReducer } from "react";


export interface Todo {
  id: number; todoName: string; todoDate: string;
}
/** REDUCER */
type Action =
  | { type: 'INIT'; payload: Todo[] }
  | { type: 'DELETE'; payload: { id: number } };

export function listReducer(currentList: Todo[], action: Action) {
  switch (action.type) {
    case "INIT":
      return action.payload;
    case "DELETE":
      return currentList.filter(item => item.id !== action.payload.id);
    default:
      return currentList;
  }
}

interface DataContextType {
  list: Todo[];
  dispatchList: React.Dispatch<Action>;
  status: string; 
  deletez: (id: number) => void;
}
export const DataContext = createContext<DataContextType>({
  list: [],
  dispatchList: () => {},
  status: "",
  deletez: () => {}
});


interface DataContextProviderProps {
  children: ReactNode;
}
const DataContextProvider = ({ children }: DataContextProviderProps) => {
  /** INITIAL DATA */
  const initialData: Todo[] = [
    { id: Math.random(), todoName: 'Milk', todoDate: '4/10/2020' },
    { id: Math.random(), todoName: 'Rice', todoDate: '8/10/2020' },
    { id: Math.random(), todoName: 'Chocolate', todoDate: '8/10/2020' }
  ];

  const [list, dispatchList] = useReducer(listReducer, initialData);
  // [dynamic equivalent to] const list = function listReducer(); triggered via. dispatchList();
  // similar as useState(), but with more complex state management (e.g. delete, update, etc. in same reducer)
  const [status, setStatus] = useState<string>("");
  useEffect(() => {
    setStatus(`${list.length} records found.`);
  }, [list.length]); 
  
  /** Delete Function */
  const deleteRecord = (id: number) => {
    dispatchList({
      type: "DELETE",
      payload: { id }
    });
  }
  
  return (
    <DataContext.Provider value={{
      list,
      dispatchList,
      status,
      deletez: deleteRecord
    }}>
      {children}
    </DataContext.Provider>
  );
}
export default DataContextProvider;