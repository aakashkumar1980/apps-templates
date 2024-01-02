import { createStore } from 'redux';
import { recordsListReducer } from './Reducer';

const store = createStore(recordsListReducer);
export default store;
