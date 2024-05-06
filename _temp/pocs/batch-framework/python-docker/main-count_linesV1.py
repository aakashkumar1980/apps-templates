import os
import multiprocessing

def count_lines_in_chunk(chunk):
  """
  Count the number of lines in a chunk of text.

  Args:
      chunk (list): A chunk of text as a list of lines.

  Returns:
      int: The number of lines in the chunk.
  """
  line_count = 0
  for _ in chunk:
    line_count += 1
  return line_count

def count_lines(filename):
  """
  Count the total number of lines in a file using multiprocessing.

  Args:
      filename (str): The path to the file.

  Returns:
      int: The total number of lines in the file.
  """
  total_lines = 0
  chunk_size = 1000  # Adjust this value based on your system's memory and performance
  with open(filename, 'r') as file:
    pool = multiprocessing.Pool(processes=multiprocessing.cpu_count())
    # Read the file in chunks
    chunks = iter(lambda: file.readlines(chunk_size), [])
    # Count lines in each chunk in parallel
    results = pool.map(count_lines_in_chunk, chunks)
    # Sum up the line counts from all chunks
    total_lines = sum(results)
    pool.close()
    pool.join()
  return total_lines

if __name__ == "__main__":
                      # 2000001
  filename = "sample.txt"
  if os.path.exists(filename):
    num_lines = count_lines(filename)
    print("Total number of lines:", num_lines)
  else:
    print("File not found.")

#%%
