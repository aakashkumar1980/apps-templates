#!/bin/bash

# Start Spark Master in the background
export SPARK_IDENT_STRING=master
start-master.sh &
# wait a little for the master to fully start up
sleep 10


# Start the first Spark Worker in the background
export SPARK_IDENT_STRING=worker1
start-worker.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
# optionally, wait a bit between starting workers
sleep 5

# Start the second Spark Worker in the background
export SPARK_IDENT_STRING=worker2
start-worker.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
# optionally, wait a bit between starting workers
sleep 5

# Start the third Spark Worker in the background
#export SPARK_IDENT_STRING=worker3
#start-worker.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077 &
#sleep 5
