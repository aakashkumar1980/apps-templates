import { Record } from '../DataModel';

// Define specific action types as an enum
export enum ActionTypes {
  GET_RECORDS = 'GET_RECORDS',
  DELETE_RECORD = 'DELETE_RECORD'
}

// Define the shape of each action using interfaces
interface getRecordsAction {
  type: ActionTypes.GET_RECORDS;
  payload: Record[];
}

interface deleteRecordAction {
  type: ActionTypes.DELETE_RECORD;
  payload: { id: string };
}

// Use a type union for the reducer actions
export type Actions = getRecordsAction | deleteRecordAction;

