import os
import json

target_memory = 5
original_file = "customers-100.json"

#target_file_size = 1024 * 1024 * 1024  # 1 GB in bytes
target_file_size = 1024 * 1024 * 1024 * target_memory

# Read original JSON data
with open(original_file, 'r') as f:
  original_data = json.load(f)

# Calculate the size of original JSON data
original_size = len(json.dumps(original_data).encode('utf-8'))

# Calculate the number of times to duplicate data
duplicate_count = target_file_size // original_size
print("Number of times to duplicate:", duplicate_count)

# Create new JSON file with duplicated data
with open(f"customers-{target_memory}GB.json", 'w') as f:
  for i in range(duplicate_count):
    json.dump(original_data, f)

# Check the total size of generated file
total_size = os.path.getsize(f"customers-{target_memory}GB.json")
print("Total size of generated file:", total_size)
