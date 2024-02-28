def apply_configs(spark, connectionMaximum, threadsMax):
    # Set essential S3 configurations
    spark.conf.set("spark.hadoop.fs.s3a.logging.level", "DEBUG")
    spark.conf.set("spark.hadoop.fs.s3a.connection.ssl.enabled", "true")
    spark.conf.set("spark.hadoop.fs.s3a.path.style.access", "true")

    spark.conf.set("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
    spark.conf.set("spark.hadoop.fs.s3a.block.size", "268435456")  # Set to 256 MB
    spark.conf.set("spark.hadoop.fs.s3a.connection.maximum", str(connectionMaximum))
    spark.conf.set("spark.hadoop.fs.s3a.threads.max", str(threadsMax))
    
    spark.conf.set("spark.hadoop.fs.s3a.connection.timeout", "20000")
    spark.conf.set("spark.hadoop.fs.s3a.connection.establish.timeout", "5000")
    spark.conf.set("spark.hadoop.fs.s3a.socket.timeout", "20000")
    spark.conf.set("spark.hadoop.fs.s3a.max.retries", "10")
    spark.conf.set("spark.hadoop.fs.s3a.retry.limit", "10")
    spark.conf.set("spark.hadoop.fs.s3a.attempts.maximum", "10")

    # Configurations for downloads
    spark.conf.set("spark.hadoop.fs.s3a.read.ahead.range", "4194304")  # 4 MB
    spark.conf.set("spark.hadoop.fs.s3a.metadata.cache.enable", "true")
    spark.conf.set("spark.hadoop.fs.s3a.metadata.cache.ttl", "300000")  # 5 minutes

    # Configurations for uploads
    spark.conf.set("spark.hadoop.fs.s3a.fast.upload", "true")
    spark.conf.set("spark.hadoop.fs.s3a.fast.upload.buffer", "disk")
    spark.conf.set("spark.hadoop.fs.s3a.buffer.dir", "/mnt/tmp")

    spark.conf.set("spark.hadoop.fs.s3a.multipart.size", "104857600")  # 100 MB
    spark.conf.set("spark.hadoop.fs.s3a.multipart.threshold", "104857600")  # 100 MB

    spark.conf.set("spark.hadoop.fs.s3a.multipart.purge", "false")
    spark.conf.set("spark.hadoop.fs.s3a.multipart.purge.age", "86400")  # 1 day

    return spark