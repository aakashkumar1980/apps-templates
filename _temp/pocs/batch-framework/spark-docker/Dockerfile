FROM bitnami/spark:latest

# Switch to the root user to perform installation tasks
USER root

# Perform necessary operations as root here
# For example, creating directories, changing permissions, installing packages
RUN mkdir -p /var/lib/apt/lists/partial && \
  chmod 755 /var/lib/apt/lists /var/lib/apt/lists/partial && \
  apt-get clean && \
  apt-get update

# [Install Python old version to be compatible with Spark]
RUN apt-get update && \
  apt-get install -y build-essential libssl-dev zlib1g-dev libbz2-dev \
  libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev \
  xz-utils tk-dev libffi-dev liblzma-dev python3-openssl git && \
  curl https://pyenv.run | bash && \
  echo 'export PYENV_ROOT="$HOME/.pyenv"' >> ~/.bashrc && \
  echo 'export PATH="$PYENV_ROOT/bin:$PATH"' >> ~/.bashrc && \
  echo 'eval "$(pyenv init -)"' >> ~/.bashrc && \
  echo 'eval "$(pyenv virtualenv-init -)"' >> ~/.bashrc && \
  export PYENV_ROOT="$HOME/.pyenv" && \
  export PATH="$PYENV_ROOT/bin:$PATH" && \
  eval "$(pyenv init -)" && \
  eval "$(pyenv virtualenv-init -)" && \
  pyenv install 3.7.9 && \
  pyenv global 3.7.9

# [Install diagnostic tools]
RUN apt-get update && \
  apt-get install -y net-tools iproute2



# Switch back to the original user (Bitnami containers typically use 1001)
USER 1001
