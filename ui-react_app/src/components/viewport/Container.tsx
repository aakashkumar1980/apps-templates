import './Viewport.css';
import React from 'react';

interface ContainerProps {
  children: React.ReactNode;
  className?: string;
}

const Container: React.FC<ContainerProps> = (props) => {
  const { children, className } = props;
  const combinedClassName = `container-style ${className || ''}`;

  return (
    <div id="container" className={combinedClassName}>
      {children}
    </div>
  );
};
export default Container;