import "./Viewport.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Content from "./content/Content";
import Footer from "./Footer";
import Header from "./Header";
import Navigation from "./Navigation";

function Viewport() {
  return (
    <div id="viewport">
      <Navigation />
      <div id="content-wrapper">
        <Header />
        <Content />
        <Footer />
      </div>
    </div>
  );
}

export default Viewport;