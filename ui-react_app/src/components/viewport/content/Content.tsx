import Dashboard from "./pages/dashboard/Dashboard";
import DataContextProvider from '../../store/DataStore';
import CreateDataRecord from './pages/create-datarecord/CreateDataRecord';

interface ContentProps {
  page: string;
}
function Content({ page }: ContentProps) {
  const renderPage = () => {
    switch (page) {
      case "Home":
        return <Dashboard />;
      case "Create DataRecord":
        return <CreateDataRecord />;

      default:
        return <Dashboard />;
    }
  }

  return (
    <div id="content">
      <DataContextProvider>
        {renderPage()}
      </DataContextProvider>
    </div>
  );
}

export default Content;