from datetime import time

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
      results = pool.imap_unordered(count_lines_in_chunk, iter(lambda: file.readlines(chunk_size), []))
      total_lines = sum(results)
      pool.close()
      pool.join()
    return total_lines

#################
### MAIN CODE ###
#################
if __name__ == "__main__":
  filename = "./_data/customers-32000000.csv"
  start_time = time.time()
  num_lines = count_lines(filename)
  end_time = time.time()

  # print the execution time in hh:mm:ss format
  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
  print("Total number of lines:", num_lines)
