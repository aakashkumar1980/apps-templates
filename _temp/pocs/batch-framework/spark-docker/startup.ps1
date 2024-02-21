$numvCPU = 4
$numMemoryGB = 6
$numWorkers = 6

# ##### STARTUP SCRIPT #####
# compile the Dockerfile and create a custom image
docker build -t custom-spark:v1 .

$executorMemoryGB = [Math]::Round(0.75 * $numMemoryGB)
$calculatedWorkerMemory = $executorMemoryGB
$numvCPU, $numWorkers, $calculatedWorkerMemory | ForEach-Object { $env:$_ = $_ }
Write-Host "Starting DockerCompose with $numvCPU vCPU, $calculatedWorkerMemory GB memory, and $numWorkers workers"
docker-compose up -d


Start-Sleep -Seconds 30
# ##### POST_STARTUP SCRIPT #####
./post-startup.ps1
