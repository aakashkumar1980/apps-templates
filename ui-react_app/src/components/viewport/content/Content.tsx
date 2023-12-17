import styles from './Content.module.scss';
import Dashboard from "./pages/dashboard/Dashboard";
import DataContextProvider from '../../store/DataStore';
import CreateDataRecord from './pages/create-datarecord/CreateDataRecord';

function Content() {
  return (
    <div id="content">
      <DataContextProvider>
        <Dashboard />
        <CreateDataRecord />
      </DataContextProvider>
    </div>
  );
}

export default Content;