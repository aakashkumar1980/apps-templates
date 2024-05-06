import dask.dataframe as dd

def count_lines(filename):
  if filename.endswith('.json'):
    df = dd.read_json(filename, lines=True)
  elif filename.endswith('.dat'):
    # Assuming the file is a delimited text file (adjust parameters as needed)
    df = dd.read_csv(filename, delimiter='\t')  # Example: Tab-delimited file
  else:
    # Assume it's a text file
    df = dd.read_text(filename)
  return len(df)

if __name__ == "__main__":
  filename = "./_data/testfile.org-5GB.dat"
  num_lines = count_lines(filename)
  print("Total number of lines:", num_lines)

