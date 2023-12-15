import React, { ReactNode, createContext } from "react";

interface TodoContextType {
  list: React.ReactNode[];
  deletez: (id: number) => void;
}

export const TodoContext = createContext<TodoContextType>({
  list: [],
  deletez: () => {}
});