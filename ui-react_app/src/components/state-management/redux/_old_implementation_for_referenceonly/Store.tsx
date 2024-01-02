import { createStore } from 'redux';
import { recordsListReducer } from './Reducer';

const recordsListStore = createStore(recordsListReducer);
export default recordsListStore;
