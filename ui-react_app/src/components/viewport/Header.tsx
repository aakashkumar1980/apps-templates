function Header() {
  let application_type = 'UI';
  let application_tech = () => { return 'React App'; }

  return (
    <div id="header">
      <div id="title" style={{ textAlign: "center" }}>
        Welcome to {application_type} {application_tech()}
      </div>

      <div style={{ width: "100vw" }} className="container">
        <header style={{ textAlign: "right" }} className="py-3 mb-4 border-bottom">
          <div className="btn-group">
            <button id="login" type="button" className="btn btn-outline-danger" disabled>Login</button>
            <button id="signup" type="button" className="btn btn-outline-danger active">Sign-up</button>
          </div>
        </header>
      </div>

    </div>
  );
}

export default Header;