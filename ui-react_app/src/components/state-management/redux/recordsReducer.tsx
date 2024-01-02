import { Record } from '../DataModel';
import { Action, ActionTypes } from './Action';

const initialState: Record[] = [];
function recordsReducer(state = initialState, action: Action): Record[] {
  switch (action.type) {
    case ActionTypes.GET_RECORDS:
      return action.payload;

    case ActionTypes.ADD_RECORD:
      return [...state, action.payload];

    case ActionTypes.DELETE_RECORD:
      return state.filter(record => record.id !== action.payload.id);

    default:
      return state;
  }
}

export default recordsReducer;
