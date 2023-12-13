import styles from './Datarecord.module.scss';

// <></> : it is equivant to the <React.Fragment></React.Fragment> shortcut tag
function Record ({todoName, todoDate, onDelete}: {todoName: string, todoDate: string, onDelete: (name: string) => void}) {
   
  return (
    <>
      <div id="record" className="row">
        <div id="todo" className="col-md-6">
          {todoName}
        </div>
        <div id="date" className="col-md-4">
          {todoDate}
        </div>
        <div className="col-md-2">
          <button className="btn btn-danger" onClick={() => onDelete(todoName)}>Delete</button>
        </div>
      </div>
    </>
  );
}

export default Record;