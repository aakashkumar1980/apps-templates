import os

target_memory = 1
original_file = "customers-100.csv"


#target_file_size = 1024 * 1024 * 1024  # 1 GB in bytes
target_file_size = 1024 * 1024 * 1024 * target_memory
# Read original file content
with open(original_file, 'r') as f:
  original_content = f.read()

# Get the size of original content
original_size = len(original_content.encode('utf-8'))

# Calculate the number of times to duplicate content
duplicate_count = target_file_size // original_size
print("Number of times to duplicate:", duplicate_count)

# Create new file with duplicated content
with open("customers-"+str(target_memory)+"GB.csv", 'w') as f:
  for i in range(duplicate_count):
    f.write(original_content)

# Check the total size of generated file
total_size = os.path.getsize("customers-"+str(target_memory)+"GB.csv")
print("Total size of generated file:", total_size)
