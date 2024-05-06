import dask.dataframe as dd

def count_lines(filename):
  if filename.endswith('.json'):
    df = dd.read_json(filename, lines=False)
  elif filename.endswith('.csv'):
    df = dd.read_csv(filename, delimiter=',')
  return len(df)

if __name__ == "__main__":
  filename = "./_data/customers-100.json"
  num_lines = count_lines(filename)
  print("Total number of lines:", num_lines)

