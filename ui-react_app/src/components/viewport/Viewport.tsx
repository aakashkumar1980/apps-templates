import "./Viewport.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import Content from "./content/Content";
import Footer from "./Footer";
import Header from "./Header";
import Navigation from "./Navigation";
import { useState } from "react";

function Viewport() {
  const [page, selectedPage] = useState("Home");
  
  return (
    <>
      <div id="viewport">
        <Navigation page={page} selectedPage={selectedPage} />
        <div id="content-wrapper">
          <Header />
          <Content page={page} />
          <Footer />
        </div>
      </div>
    </>    
  );
}

export default Viewport;