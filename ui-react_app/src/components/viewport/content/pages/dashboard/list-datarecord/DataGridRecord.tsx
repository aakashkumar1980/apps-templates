import styles from './DataGrid.module.scss';

// <></> : it is equivant to the <React.Fragment></React.Fragment> shortcut tag
function DataGridRecord({ id, todoName, todoDate }:
  { id: string, todoName: string, todoDate: string }) {
  return (
    <>
      <div id={`datagridrecord-${id}`} className="row">
        <div id="todo" className="col-md-6">
          {todoName}
        </div>
        <div id="date" className="col-md-4">
          {todoDate}
        </div>
      </div>
    </>
  );
}

export default DataGridRecord;