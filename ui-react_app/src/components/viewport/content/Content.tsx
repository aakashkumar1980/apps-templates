import styles from './Content.module.scss';
import Dashboard from "./pages/dashboard/Dashboard";

function Content() {
  return (
    <div id="content" className={`${styles.content}`}>
      <Dashboard />
    </div>
  );
}

export default Content;