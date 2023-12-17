import Container from './Container';

interface ContentProps {
  page: string;
  selectedPage: (page: string) => void;
}
function Navigation({ page, selectedPage }: ContentProps) {
  return (
    <div id="navigation">
      <Container>
        <div className="d-flex flex-column flex-shrink-0 p-3 text-bg-danger">
          <a href="/" className="d-flex align-items-center mb-3 mb-md-0 me-md-auto text-white text-decoration-none">
            <span className="fs-4">Menus</span>
          </a>
          <hr />

          <ul className="nav nav-pills flex-column mb-auto">
            <li onClick={() => selectedPage("Home")}>
              <a href="#" className={`nav-link text-white ${page === 'Home' && "active"}`}>
                <span className="bi bi-house-door">&nbsp; Home</span>
              </a>
            </li>
            <li onClick={() => selectedPage("Create DataRecord")}>
              <a href="#" className={`nav-link text-white ${page === 'Create DataRecord' && "active"}`}>
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
                  <span className="bi bi-box-arrow-right">&nbsp; Logout</span>
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