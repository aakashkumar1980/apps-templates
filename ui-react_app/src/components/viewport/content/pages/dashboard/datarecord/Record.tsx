import styles from '../Dashboard.module.scss';

// <></> : it is equivant to the <React.Fragment></React.Fragment> shortcut tag
function Record ({todoName, todoDate}: {todoName: string, todoDate: string}) {
  return (
    <>
      <div id="rowdata" className={`${styles.rowdata} row`}>
        <div id="todo" className="col-md-6">
          {todoName}
        </div>
        <div id="date" className="col-md-4">
          {todoDate}
        </div>
        <div className="col-md-2">
          <button className="btn btn-danger">Delete</button>
        </div>
      </div>
    </>
  );
}

export default Record;