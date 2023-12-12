interface RecordWrapperProps {
  value: React.ReactNode;
}
const RecordWrapper: React.FC<RecordWrapperProps> = ({ value }) => {
  return <div id="1">{value}</div>;
};

export default RecordWrapper;