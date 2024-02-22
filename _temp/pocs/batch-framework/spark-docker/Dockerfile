FROM bitnami/spark:3.3.4

# Define the Hadoop version to match your Hadoop cluster
ENV HADOOP_VERSION 3.3.6

USER root

# Install dependencies
RUN apt-get update && \
  apt-get install -y net-tools iproute2 iputils-ping net-tools curl wget telnet netcat-openbsd python3-pip && \
  pip3 install --upgrade pip && \
  pip3 install boto3 && \
  install_packages nano

# Download and unpack Hadoop to get the client libraries
RUN wget --no-check-certificate https://downloads.apache.org/hadoop/common/hadoop-$HADOOP_VERSION/hadoop-$HADOOP_VERSION.tar.gz -P /tmp/ && \
  tar xzf /tmp/hadoop-$HADOOP_VERSION.tar.gz -C /opt/ && \
  rm /tmp/hadoop-$HADOOP_VERSION.tar.gz

# Set Hadoop home environment variable
ENV HADOOP_HOME=/opt/hadoop-$HADOOP_VERSION
ENV PATH=$PATH:$HADOOP_HOME/bin

# Copy the Hadoop configuration files from your Hadoop setup if you have custom configurations.
# It's essential to have these for HDFS access. Adjust the source paths as necessary.
COPY ./conf/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml
COPY ./conf/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml

# Ensure permissions are set correctly for the Spark user
RUN chown -R 1001:1001 $HADOOP_HOME

# Create a test workspace & temp directory
RUN mkdir -p /var/poc-workspace && \
  chown -R 1001:1001 /var/poc-workspace
RUN mkdir -p /mnt/tmp && chmod a+w /mnt/tmp


# Switch back to the default user
USER 1001
