import { Record } from '../DataModel';
import { Actions, ActionTypes } from './Actions';

interface State {
  recordsList: Record[];
}
const initialState: State = {
  recordsList: []
};

export const recordsListReducer = (state: State = initialState, action: Actions): State => {
  switch (action.type) {
    case ActionTypes.GET_RECORDS:
      // replace the entire recordsList with the new one
      return { ...state, recordsList: action.payload };

    case ActionTypes.DELETE_RECORD:
      // filter out the record to delete
      return { 
        ...state, 
        recordsList: state.recordsList.filter(record => record.id !== action.payload.id)
      };

    default:
      // Return the current state if no action is matched
      return state;
  }
};