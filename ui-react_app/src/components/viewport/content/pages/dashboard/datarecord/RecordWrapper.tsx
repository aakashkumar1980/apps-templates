import styles from '../Dashboard.module.scss';

interface RecordWrapperProps {
  value: React.ReactNode;
}
const RecordWrapper: React.FC<RecordWrapperProps> = ({ value }) => {
  return <div>{value}</div>;
};

export default RecordWrapper;