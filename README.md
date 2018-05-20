# SparkTuning
an cost-based optimizer for Spark programs with different complexity
## How to run
1. compile and package code/spark-1.4.0
2. install code/spark-1.4.0 to your cluster 
3. shrink input data and sumbit an application to spark cluster
4. feed the application runtime logs to code/WhatIf, get the best config
5. rerun the application with the best config and the whole input data
