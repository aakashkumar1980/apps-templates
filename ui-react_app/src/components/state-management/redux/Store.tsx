// store/store.tsx
import { createStore } from 'redux';
import recordsReducer from './recordsReducer';

const store = createStore(recordsReducer);
export default store;
