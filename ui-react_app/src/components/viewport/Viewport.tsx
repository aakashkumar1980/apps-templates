import Content from "./content/Content";
import Footer from "./footer/Footer";
import Header from "./header/Header";
import Navigation from "./navigation/Navigation";
import "./Viewport.css";

function Viewport() {
  return (
    <div id="viewport" className="viewport">
      <div id="viewport_header">
        <Header />
      </div>
      <div id="viewport_center">
        <div id="viewport_navigation">
          <Navigation />
        </div>
        <div id="viewport_content">
          <Content />
        </div>
      </div>
      <div id="viewport_footer">
        <Footer />
      </div>
    </div>
  );
}

export default Viewport;