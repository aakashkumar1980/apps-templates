FROM bitnami/spark:latest

# Switch to the root user to perform installation tasks
USER root

RUN mkdir -p /var/lib/apt/lists/partial && \
  chmod 755 /var/lib/apt/lists /var/lib/apt/lists/partial && \
  apt-get clean && \
  apt-get update

# Install dependencies for Python installation
RUN apt-get update && \
  apt-get install -y build-essential libssl-dev zlib1g-dev libbz2-dev \
  libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev \
  xz-utils tk-dev libffi-dev liblzma-dev python3-openssl git

# Install pyenv
RUN curl https://pyenv.run | bash

# Configure environment variables for PyEnv directly to ensure they are applied for all shell sessions
ENV PYENV_ROOT="$HOME/.pyenv"
ENV PATH="$PYENV_ROOT/bin:$PATH"

# Initialize PyEnv and install Python 3.7.9
RUN eval "$(pyenv init -)" && \
  eval "$(pyenv virtualenv-init -)" && \
  pyenv install 3.7.9 && \
  pyenv global 3.7.9

# Print the Python version to verify the installation
RUN python --version

# Install diagnostic tools
RUN apt-get update && \
  apt-get install -y net-tools iproute2


# Switch back to the original user (Bitnami containers typically use 1001)
USER 1001
