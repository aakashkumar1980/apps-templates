class FileComparator:
  def __init__(self, raw_file_path, decrypted_file_path):
    self.raw_file_path = raw_file_path
    self.decrypted_file_path = decrypted_file_path

  def get_line(self, file_path, line_number):
    with open(file_path, 'r') as file:
      for current_line_number, line in enumerate(file):
        if current_line_number == line_number:
          return line
    return None

  def get_last_line_number(self, file_path):
    with open(file_path, 'r') as file:
      for line_number, line in enumerate(file):
        pass
    return line_number

  def compare_files(self):
    # Get the last line number to determine file length
    raw_last_line_number = self.get_last_line_number(self.raw_file_path)
    decrypted_last_line_number = self.get_last_line_number(self.decrypted_file_path)

    if raw_last_line_number != decrypted_last_line_number:
      print("Files do not have the same number of lines.")
      return

    middle_line_number = raw_last_line_number // 2

    # Get the lines to compare
    lines_to_compare = {
      'first': 0,
      'middle': middle_line_number,
      'last': raw_last_line_number
    }

    comparisons = {}

    for position, line_number in lines_to_compare.items():
      raw_line = self.get_line(self.raw_file_path, line_number)
      decrypted_line = self.get_line(self.decrypted_file_path, line_number)
      comparisons[position] = (raw_line, decrypted_line)

    self.print_comparisons(comparisons)

  def print_comparisons(self, comparisons):
    for position, (raw_line, decrypted_line) in comparisons.items():
      print(f"Comparison for {position} line:")
      print(f"Raw: {raw_line.strip()}")
      print(f"Decrypted: {decrypted_line.strip()}")
      print(f"Match: {raw_line == decrypted_line}")
      print()

# Example usage
raw_file_path = '/mnt/ebs_volume/tmp/_data/customers-128000000.csv'
decrypted_file_path = raw_file_path.replace('.csv', '_decrypted.csv')

comparator = FileComparator(raw_file_path, decrypted_file_path)
comparator.compare_files()


# RESULTS #:
## File: customers-128000000.csv & customers-128000000_decrypted.csv
#### CPU: 16 cores | 32 vCPU (% usage)
#### RAM:
#### Time:
