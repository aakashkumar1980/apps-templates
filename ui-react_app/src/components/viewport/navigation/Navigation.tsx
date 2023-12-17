import styles from './Navigation.module.scss';
import Container from '../Container';

function Navigation() {
  return (
    <div id="navigation">
      <Container>
        <div style={{ width: "100%" }} className="d-flex flex-column flex-shrink-0 p-3 text-bg-danger">
          <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <span className="fs-4">Menus</span>
          </a>
          <hr />

          <ul className="nav nav-pills flex-column mb-auto">
            <li>
              <a href="#" className="nav-link text-white active">
                <span className="bi bi-house-door">&nbsp; Home</span>
              </a>
            </li>
            <li>
              <a href="#" className="nav-link text-white">
                <span className="bi bi-vinyl-fill">&nbsp; Create DataRecord</span>
              </a>
            </li>
          </ul>
          <hr />
          <div>
            <ul className="nav nav-pills flex-column mb-auto shadow">
              <li>
                <a href="#" className="nav-link text-white">
                  <span className="bi bi-person-lines-fill">&nbsp; Profile</span>
                </a>
              </li>
              <li>
                <a href="#" className="nav-link text-white">
                  <span className="bi bi-box-arrow-right">&nbsp; Signout</span>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default Navigation;