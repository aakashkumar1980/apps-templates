import os
import multiprocessing

def count_lines(segment):
  """
  Counts the number of lines in a list of lines.

  Parameters:
  segment (list): The list of lines in the file segment.

  Returns:
  int: The number of lines in the list.
  """
  return len(segment)

def prepare_segment(segment_index, segment_size, file_path):
  """
  Reads a segment of the file and returns its content.

  Parameters:
  segment_index (int): The index of the segment.
  segment_size (int): The size of each segment.
  file_path (str): The path to the file.

  Returns:
  list: The lines in the segment.
  """
  start_line = segment_index * segment_size
  lines = []
  with open(file_path, 'r') as file:
    # Skip lines until reaching the start line of the segment
    for _ in range(start_line):
      file.readline()
    # Read lines up to the segment size or end of file
    for _ in range(segment_size):
      line = file.readline().strip()
      if not line:
        break
      lines.append(line)
  return lines

if __name__ == "__main__":
  # Define file paths
  file_path = 'sample.txt'

  ### LINE COUNT ###
  # Determine CPU count
  cpu_count = multiprocessing.cpu_count()
  print("CPU count:", cpu_count)

  # Read all lines from the file
  with open(file_path, 'r') as file:
    all_lines = file.readlines()
  total_lines = len(all_lines)

  # Calculate segment size based on total number of lines
  segment_size = total_lines // cpu_count

  # Prepare file segments
  segments = []
  with multiprocessing.Pool(cpu_count) as pool:
    segment_indexes = range(cpu_count)
    results = [pool.apply_async(prepare_segment, (index, segment_size, file_path)) for index in segment_indexes]
    for result in results:
      segment = result.get()
      segments.append(segment)

  # Handle remainder lines
  if total_lines % cpu_count != 0:
    remainder_lines = all_lines[total_lines - total_lines % cpu_count:]
    segments[-1].extend(remainder_lines)

  # Start multiprocessing for line counting
  with multiprocessing.Pool(cpu_count) as pool:
    line_counts = pool.map(count_lines, segments)

  total_lines = sum(line_counts)
  print("Total lines:", total_lines)
