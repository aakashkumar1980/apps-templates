import recordsListReducer from './Reducer';
import { configureStore } from '@reduxjs/toolkit';


const recordsListStore = configureStore({
  reducer: {
    recordsList: recordsListReducer
  }
});
export default recordsListStore;
