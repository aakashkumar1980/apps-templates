import dask.dataframe as dd

def count_lines(filename):
  df = dd.read_csv(filename)
  return len(df)

if __name__ == "__main__":
  filename = "./_data/customers-100.csv"
  num_lines = count_lines(filename)
  print("Total number of lines:", num_lines)

