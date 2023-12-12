import styles from './Header.module.scss';

function Header() {
  let application_type = 'UI';
  let application_tech = () => {return 'React App';}

  console.log(styles);
  return (
    <div id="header" className={`${styles["header"]}`}>
      Welcome to {application_type} {application_tech()}
    </div>
  );
}

export default Header;