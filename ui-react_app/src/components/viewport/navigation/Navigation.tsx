import styles from './Navigation.module.scss';
import Container from '../Container';

function Navigation() {
  return (
    <div id="navigation">
      <Container>
      <div style={{ width: "100%" }}className="d-flex flex-column flex-shrink-0 p-3 text-bg-danger">
        <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
          <span className="fs-4">Menus</span>
        </a>
        <hr />
        <ul className="nav nav-pills flex-column mb-auto">
          <li className="nav-item">
            <a href="#" className="nav-link active" aria-current="page">
              <svg className="bi pe-none me-2" width={16} height={16}>
                <use xlinkHref="#home" />
              </svg>
              Home
            </a>
          </li>
          <li>
            <a href="#" className="nav-link text-white">
              <svg className="bi pe-none me-2" width={16} height={16}>
                <use xlinkHref="#record" />
              </svg>
              Create DataRecord
            </a>
          </li>
        </ul>
        <hr />
        <div>
          <ul className="nav nav-pills flex-column mb-auto shadow">
            <li>
              <a href="#" className="nav-link text-white">
                <svg className="bi pe-none me-2" width={16} height={16}>
                  <use xlinkHref="#profile" />
                </svg>
                Profile
              </a>
            </li>
            <li>
              <a href="#" className="nav-link text-white">
                <svg className="bi pe-none me-2" width={16} height={16}>
                  <use xlinkHref="#signout" />
                </svg>
                Sign Out
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