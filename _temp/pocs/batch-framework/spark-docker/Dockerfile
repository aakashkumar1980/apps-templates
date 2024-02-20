FROM bitnami/spark:3.3.4

# Switch to the root user to perform installation tasks
USER root

# Install PING and other network tools (like checkin the connection on ports to other servers) for debugging
RUN apt-get update && \
  apt-get install -y net-tools iproute2 iputils-ping net-tools curl wget netcat  

# Create a test workspace directory
RUN mkdir -p /var/test-workspace

# Switch back to the original user (Bitnami containers typically use 1001)
USER 1001
