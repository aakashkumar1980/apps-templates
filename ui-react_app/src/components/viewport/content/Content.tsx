import styles from './Content.module.scss';
import Dashboard from "./pages/dashboard/Dashboard";
import DataContextProvider from '../../store/DataStore';

function Content() {
  return (
    <div id="content" className={`${styles.content}`}>
      <DataContextProvider>
        <Dashboard />
      </DataContextProvider>
    </div>
  );
}

export default Content;