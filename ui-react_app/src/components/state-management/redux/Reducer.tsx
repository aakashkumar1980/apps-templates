import { Record } from '../DataModel';
import { Actions, ActionTypes } from './Actions';

const initialState: Record[] = [];
export const recordsListReducer = (state = initialState, actions: Actions): Record[] => {
  switch (actions.type) {
    case ActionTypes.GET_RECORDS:
      return actions.payload;

    case ActionTypes.DELETE_RECORD:
      return state.filter(record => record.id !== actions.payload.id);

    default:
      return state;
  }
};
