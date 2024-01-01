import { Routes, Route } from 'react-router-dom';
import Dashboard from './pages/dashboard/Dashboard';
import CreateDataRecord from './pages/create-datarecord/CreateDataRecord';
import DataContextProvider from '../../state-management/context/DataContextProvider';

function Content() {
  return (
    <div id="content">
      <DataContextProvider>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/create-datarecord" element={<CreateDataRecord />} />
        </Routes>
      </DataContextProvider>
    </div>
  );
}

export default Content;
