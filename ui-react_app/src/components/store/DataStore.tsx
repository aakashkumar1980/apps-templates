import React, { ReactNode, createContext } from "react";

interface DataContextType {
  list: React.ReactNode[];
  deletez: (id: number) => void;
}
export const DataContext = createContext<DataContextType>({
  list: [],
  deletez: () => {}
});