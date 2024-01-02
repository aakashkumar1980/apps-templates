import { Record } from '../DataModel';
import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface State {
  recordsList: Record[];
}
const initialState: State = {
  recordsList: []
};

const recordsListSlice = createSlice({
  name: 'recordsList',
  initialState,
  reducers: {
    getRecords: (state, action: PayloadAction<Record[]>) => {
      state.recordsList = action.payload;
    },
    deleteRecord: (state, action: PayloadAction<Record>) => {
      state.recordsList = state.recordsList.filter(record => record.id !== action.payload.id);
    }
  }
});
export const { getRecords, deleteRecord } = recordsListSlice.actions;
export default recordsListSlice.reducer;