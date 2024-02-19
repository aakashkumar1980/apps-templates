package com.aadityadesigners.poc.fileupdate;

import org.apache.spark.sql.SparkSession;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import com.aadityadesigners.poc.Utils;

public class FileColumnUpdateApp {
  private static final Logger LOGGER = Logger.getLogger(FileColumnUpdateApp.class);

  public static void main(String[] args) {
    LOGGER.info("##### Starting Application #####");
    SparkSession spark = SparkSession
        .builder()
        .appName("FileColumnUpdateApp")
        .master(String.format("spark://%s:7077", Utils.HOSTNAME))
        .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .getOrCreate();

    StructType schema = new StructType(new StructField[] {
        DataTypes.createStructField("Index", DataTypes.IntegerType, false),
        DataTypes.createStructField("OrganizationId", DataTypes.StringType, false),
        DataTypes.createStructField("Name", DataTypes.StringType, false),
        DataTypes.createStructField("Website", DataTypes.StringType, false),
        DataTypes.createStructField("Country", DataTypes.StringType, false),
        DataTypes.createStructField("Description", DataTypes.StringType, false),
        DataTypes.createStructField("Founded", DataTypes.IntegerType, false),
        DataTypes.createStructField("Industry", DataTypes.StringType, false),
        DataTypes.createStructField("NumberOfEmployees", DataTypes.IntegerType, false)
    });

    long startTotalTime = System.currentTimeMillis();
    String fileName = "dataV2.csv";
    String filePath = "/home/ubuntu/Downloads/";
    // Read the CSV file into a Dataset<Row>
    long startTimeReadFile = System.currentTimeMillis();
    Dataset<Row> df = spark.read()
        .option("header", "true")
        .schema(schema)
        .csv(filePath + fileName); // Adjust path accordingly
    long endTimeReadFile = System.currentTimeMillis();
    LOGGER
        .info(
            "##### Total time taken for Read CSV is " + ((endTimeReadFile - startTimeReadFile) / 1000.0) + " seconds");

    // Increment the "Number of employees" by 10,000,000
    long startTimeUpdateDf = System.currentTimeMillis();
    Dataset<Row> updatedDf = df.withColumn(
        "NumberOfEmployees",
        functions.col("NumberOfEmployees").plus(10000000));
    long endTimeUpdateDf = System.currentTimeMillis();
    LOGGER
        .info(
            "##### Total time taken for UpdateIncrementDf is " + ((endTimeUpdateDf - startTimeUpdateDf) / 1000.0)
                + " seconds");

    // Select the required columns
    long startTimeSelectResultDf = System.currentTimeMillis();
    Dataset<Row> resultDf = updatedDf.select("Index", "OrganizationId", "NumberOfEmployees");
    long endTimeSelectResultDf = System.currentTimeMillis();
    LOGGER
        .info("##### Total time taken for SelectResultDf is "
            + ((endTimeSelectResultDf - startTimeSelectResultDf) / 1000.0)
            + " seconds");

    // Write the result to a new CSV file
    int count = (int) resultDf.count();
    long startWriteFileDf = System.currentTimeMillis();
    resultDf.write()
        .option("header", "true")
        .csv(filePath + fileName + "-updated.csv"); // Adjust output path accordingly
    long endWriteFileDf = System.currentTimeMillis();
    LOGGER
        .info("##### Total time taken for WriteFile (" + count + ") is "
            + ((endWriteFileDf - startWriteFileDf) / 1000.0) + " seconds");

    long stopTotalTime = System.currentTimeMillis();
    LOGGER
        .info("##### Total time taken for the whole process is "
            + ((stopTotalTime - startTotalTime) / 1000.0) + " seconds");

    // Stop the Spark context
    spark.stop();
    spark.close();
  }
}
