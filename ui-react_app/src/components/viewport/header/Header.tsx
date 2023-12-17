import styles from './Header.module.scss';

function Header() {
  let application_type = 'UI';
  let application_tech = () => { return 'React App'; }

  return (
    <div id="header" className={`${styles["header"]}`}>
      <div id="title" style={{ textAlign: "center" }}>
        Welcome to {application_type} {application_tech()}
      </div>

      <div style={{ width: "100vw" }} className="container">
        <header className="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
          <div className="col-md-3 text-end">
            <button type="button" className="btn btn-outline-danger me-2">
              Login
            </button>
            <button type="button" className="btn btn-danger">
              Sign-up
            </button>
          </div>
        </header>
      </div>

    </div>
  );
}

export default Header;