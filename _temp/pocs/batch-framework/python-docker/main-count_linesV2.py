import time

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
  filename = "./_data/customers-128000000.csv"
  start_time = time.time()
  num_lines = count_lines(filename)
  end_time = time.time()

  # print the execution time in hh:mm:ss format
  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
  print("Total number of lines:", num_lines)

  ## RESULTS:
  ## File: customers-64000000.csv (11.2GB Size)
  ### CPU: 2 cores | 4 vCPU (50% usage)
  #### RAM: 1.4 GB usage
  #### Execution time (MM:HH:SS): 00:05:23

  ### CPU: 8 cores | 16 vCPU (20% usage)
  #### RAM: 5.7 GB usage
  #### Execution time (MM:HH:SS): 00:02:34



  ## File: customers-128000000.csv (22.4GB Size)
  ### CPU: 2 cores | 4 vCPU (60% usage)
  #### RAM: 1.5 GB usage
  #### Execution time (MM:HH:SS): 00:10:24

  ### CPU: 8 cores | 16 vCPU (25% usage)
  #### RAM: 5.5 GB usage
  #### Execution time (MM:HH:SS): 00:04:57
