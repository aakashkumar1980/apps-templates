# INFRA SETUP
## AWS S3
![alt text](_misc/image.png)
![alt text](_misc/image-1.png)

## SPARK BATCH PROGRAM
![alt text](<_misc/image-2.png>)

> NOTE: AWS S3 & Spark Batch Program are into different regions as shown in the above snapshots. 


<br/>
<br/>
<br/>

# PERFORMANCE METRICS

<b>
** SUMMARY ** <br/>
Since "Spark Batch" downloads files parallel across the clusters therefore the performance is best and are highly scalable horizontally. For our sample the metrics are as follows.
<br/><br/>
- Performance gain (from 50 minutes to 3 minutes): <font size="5px"><u>1,567</u>%</font>. <br/>
- Faster: <font size="5px"><u>15 times</u></font><br/>
</b>
<br/><br/>

## "Regular" File Download
When the AWS S3 file is downloaded using a regular approach via. the browser with the same machine where the 'Spark Batch Program' is running it takes "50 mins+" to download the file (105 GB).
![alt text](_misc/image-3.png)

<br/>

## "Spark Batch Program" File Download
While if the AWS S3 files are downloaded by using the Spark Cluster then it takes only "2.8 minutes" to down both the files (110 GB)
![alt text](<_misc/image-4.png>)
<br/>

Files downloaded in HDFS FileSystem and stored in a parquet format.
![alt text](<_misc/image-5.png>)

### VIDEO
["Regular" File Download](_misc/regular-download.mp4) <br/>
["Spark Batch Program" File Download](_misc/spring_batch_program.mp4)


### Program Code
In the below Spark Batch program, various spark and aws s3 optimizing arguments are passed to get the best performance.<br/> 
[Spark Batch Program](programs/download_filesv2.py)



