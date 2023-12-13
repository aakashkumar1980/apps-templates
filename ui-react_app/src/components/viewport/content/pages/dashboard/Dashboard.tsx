import styles from './Dashboard.module.scss';
import Record from "./datarecord/Record";
import DataRecords from "./datarecord/DataRecords";
import Container from '../../Container';


function Dashboard() {
  let list: JSX.Element[] = []; 
  list.push(<Record key={1} todoName={'Milk'} todoDate={'4/10/2020'} />);
  list.push(<Record key={1} todoName={'Rice'} todoDate={'8/10/2020'} />);
  list.push(<Record key={1} todoName={'Chocolate'} todoDate={'8/10/2020'} />);

  return (
    <div id="dashboard">

      <Container className="">
        <div style={{display:"block", width:"100%"}} className="toast" role="alert" aria-live="assertive" aria-atomic="true">
          <div className="toast-header">
            <strong className="me-auto">TODO App</strong>
          </div>

          <div className="toast-body">
              <DataRecords items={list}></DataRecords>
          </div>
        </div>
      </Container>

    </div>
  );
}

export default Dashboard;