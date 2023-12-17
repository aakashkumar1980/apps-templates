import Content from "./content/Content";
import Footer from "./Footer";
import Header from "./Header";
import Navigation from "./Navigation";
import "./Viewport.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';

function Viewport() {
  return (
    <>
      <div id="viewport" style={{ display: "flex" }}>
        <Navigation />
        <div>
          <Header />
          <Content />
          <Footer />
        </div>
      </div>
    </>    
  );
}

export default Viewport;