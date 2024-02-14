> Pre-Requisites:
```sh
ubuntu@ip-172-31-7-170:~$ echo $SPARK_HOME
/opt/spark/spark-2.4.8-bin-hadoop2.7

ubuntu@ip-172-31-7-170:~$ java -version
openjdk version "1.8.0_402"
```

<br/>
<br/>

# S3FileDownload

## Regular Download
Using browser to download AWS S3 file (105 GB) tooks around <b><font color="red">20 minutes</font></b>.

## Spark Cluster Download
Using Spark to download AWS S3 file (105 GB) tooks around <b><font color="green">3 minutes</font></b><font color="green"><sup>(500% performance gain)</sup></font>.
  