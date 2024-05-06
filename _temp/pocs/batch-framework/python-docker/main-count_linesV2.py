import dask.dataframe as dd
import multiprocessing

def count_lines_in_chunk(chunk):
  line_count = 0
  for _ in chunk:
    line_count += 1
  return line_count

def count_lines(filename):
  if filename.endswith('.json'):
    df = dd.read_json(filename, lines=False)
    return len(df)
  elif filename.endswith('.csv'):
    df = dd.read_csv(filename, delimiter=',')
    return len(df)
  else:
    total_lines = 0
    # Adjust this value based on your system's memory and performance
    chunk_size = 1000
    with open(filename, 'r') as file:
      pool = multiprocessing.Pool(processes=multiprocessing.cpu_count())
      chunks = iter(lambda: file.readlines(chunk_size), [])
      results = pool.map(count_lines_in_chunk, chunks)
      total_lines = sum(results)
      pool.close()
      pool.join()
    return total_lines

if __name__ == "__main__":
  filename = "./_data/sample.txt"
  num_lines = count_lines(filename)
  print("Total number of lines:", num_lines)
