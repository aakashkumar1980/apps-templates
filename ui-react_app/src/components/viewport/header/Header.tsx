function Header() {
  let application_type = 'UI';
  let application_tech = () => {return 'React App';}

  return (
    <div id="header">
      Welcome to {application_type} {application_tech()}
    </div>
  );
}

export default Header;