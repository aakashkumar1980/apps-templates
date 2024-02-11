#!/bin/bash

#export SPARK_IDENT_STRING=worker3
#stop-worker.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077

export SPARK_IDENT_STRING=worker2
stop-worker.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077

export SPARK_IDENT_STRING=worker1
stop-worker.sh spark://ip-172-31-7-170.us-west-1.compute.internal:7077

export SPARK_IDENT_STRING=master
stop-master.sh
