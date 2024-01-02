import { Record } from '../../DataModel';

// Define specific action types as an enum
export enum ActionTypes {
  GET_RECORDS = 'GET_RECORDS',
  DELETE_RECORD = 'DELETE_RECORD'
}

export const getRecordsAction = (records: Record[]) => ({
  type: ActionTypes.GET_RECORDS,
  payload: records,
});

export const deleteRecordAction = (record: Record) => ({
  type: ActionTypes.DELETE_RECORD,
  payload: record,
});

// Use a type union for the reducer actions
export type Actions = ReturnType<typeof getRecordsAction> | ReturnType<typeof deleteRecordAction>;

