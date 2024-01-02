import { Record } from '../DataModel';

/** Action Types */
export enum ActionTypes {
  ADD_RECORD = 'ADD_RECORD'
}
/** Actions */
interface addRecordAction {
  type: ActionTypes.ADD_RECORD;
  payload: Record;
}

export type Actions = addRecordAction;

