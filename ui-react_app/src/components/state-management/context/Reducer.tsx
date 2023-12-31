import { Record } from '../DataModel';
import { Actions, ActionTypes } from './Actions';

const initialState: Record[] = [];
export const recordsListReducer = (state = initialState, actions: Actions): Record[] => {
  switch (actions.type) {
    case ActionTypes.ADD_RECORD:
      return [...state, actions.payload];

    default:
      return state;
  }
};