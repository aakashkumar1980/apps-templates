import { Record } from '../DataModel';
import { Action, ActionTypes } from './Action';

export const recordsListReducer: (currentRecordsList: Record[], action: Action) => Record[] = (currentRecordsList, action) => {
  const newPayload = action.payload as Record[];
  
  switch (action.type) {
    case ActionTypes.GET_RECORDS:
      return [...action.payload];

    case ActionTypes.ADD_RECORD:
      return [...currentRecordsList, action.payload];

    case ActionTypes.DELETE_RECORD:
      return currentRecordsList.filter(item => item.id !== newPayload[0].id);

    default:
      throw new Error(`Unhandled action type: ${(action as Action).type}`);
  }
};