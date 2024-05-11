import time
import dask.dataframe as dd


def count_lines(filename):
  if filename.endswith('.csv'):
    blockSize = "100MB"
    df = dd.read_csv(filename, delimiter=',', blocksize=blockSize, usecols=['Index'], dtype={'Index': 'int64'})
    return len(df)

#################
### MAIN CODE ###
#################
if __name__ == "__main__":
  filename = "./_data/customers-256000000.csv"
  print("Counting lines in file:", filename)
  start_time = time.time()
  num_lines = count_lines(filename)
  end_time = time.time()

  # print the execution time in hh:mm:ss format
  print("Execution time:", time.strftime('%H:%M:%S', time.gmtime(end_time - start_time)))
  print("Total number of lines:", num_lines)




# RESULTS #:
  ## File: customers-256000000.csv (44.7 GB size)
  ### CPU: 4 cores | 8 vCPU (60% usage)
  #### RAM: 2.1 GB
  #### Execution time (MM:HH:SS): 00:05:04
  #
  ### CPU: 8 cores | 16 vCPU (% usage)
  #### RAM:
  #### Execution time (MM:HH:SS):


