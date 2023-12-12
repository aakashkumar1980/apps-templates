import styles from './Footer.module.scss';

function Footer() {
  let year = new Date().getFullYear();

  return (
    <div id="footer">
      Copywrite &copy; {year} | <a href="https://www.aaditya-designers.com/">Aaditya Designers Pvt. Ltd.</a>
    </div>
  );
}

export default Footer;