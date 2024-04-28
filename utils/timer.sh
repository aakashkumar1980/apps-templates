#!/bin/bash

# This script is a simple timer that counts down from the specified number of minutes.
# It displays the elapsed time in MM:SS format and updates every 10 seconds.
# The script takes the total sleep time in minutes as an argument.

# Function to convert seconds to MM:SS format
convert_to_mm_ss() {
    local total_seconds=$1
    local minutes=$((total_seconds / 60))
    local seconds=$((total_seconds % 60))
    printf "%02d:%02d" $minutes $seconds
}

# Check if an argument is provided
if [ $# -eq 0 ]; then
    echo "Usage: $0 <minutes>"
    exit 1
fi

# Get the total sleep time in minutes from the argument and convert it to seconds
total_sleep_minutes=$1
total_sleep=$((total_sleep_minutes * 60))

# Interval for the counter update in seconds
interval=10

# Initialize counter
counter=0

# Use tput to save and restore cursor position
tput sc

# Start the loop
while [ $counter -lt $total_sleep ]; do
    # Convert counter to MM:SS format
    elapsed_time=$(convert_to_mm_ss $counter)

    # Restore cursor position and print elapsed time
    tput rc
    echo -ne "Elapsed Time: $elapsed_time"

    # Sleep for the interval duration
    sleep $interval

    # Increment the counter by the interval
    counter=$((counter + interval))
done

echo -e "\nCompleted ${total_sleep_minutes}-minute/s wait."
exit 0
