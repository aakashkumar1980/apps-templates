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
      if (Array.isArray(action.payload)) {
        return { ...state, recordsList: action.payload };
      }
      console.error('Invalid payload for GET_RECORDS action');
      return state;

    case ActionTypes.DELETE_RECORD:
      // filter out the record to delete
      return { 
        ...state, 
        recordsList: state.recordsList.filter(record => record.id !== (action.payload as Record).id)
      };

    default:
      // return the current state if no action is matched
      return state;
  }
};