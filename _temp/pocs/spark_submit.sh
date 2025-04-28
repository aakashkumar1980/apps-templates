#!/bin/bash

spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --executor-memory 2G \
  --executor-cores 2 \
  --num-executors 20 \
  src/driver.py
