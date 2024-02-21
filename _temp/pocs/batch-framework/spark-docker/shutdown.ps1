$numvCPU = 4
$numMemoryGB = 6
$numWorkers = 6

# ##### STOP SCRIPT #####
$executorMemoryGB = [Math]::Round(0.75 * $numMemoryGB)
$calculatedWorkerMemory = $executorMemoryGB
$numvCPU, $numWorkers, $calculatedWorkerMemory | ForEach-Object { $env:$_ = $_ }
# Stop all containers
docker-compose down


##### CEANUP #####
docker ps -q | ForEach-Object {docker stop $_}
docker ps -aq | ForEach-Object {docker rm $_}
docker images -q | ForEach-Object {docker rmi -f $_}
docker volume ls -q | ForEach-Object {docker volume rm $_}
docker system prune -f -y
docker volume prune -f -y
docker network prune -f -y
docker image prune -a -f -y

Get-ChildItem "./data/namenode_data" -Recurse | Where-Object { $_.Name -ne 'empty.txt' } | Remove-Item -Force -Recurse
Get-ChildItem "./data/datanode_data1" -Recurse | Where-Object { $_.Name -ne 'empty.txt' } | Remove-Item -Force -Recurse
Get-ChildItem "./data/datanode_data2" -Recurse | Where-Object { $_.Name -ne 'empty.txt' } | Remove-Item -Force -Recurse
Get-ChildItem "./data/spark_master" -Recurse | Where-Object { $_.Name -ne 'empty.txt' } | Remove-Item -Force -Recurse
Get-ChildItem "./data/spark_workers" -Recurse | Where-Object { $_.Name -ne 'empty.txt' } | Remove-Item -Force -Recurse
Get-ChildItem "./data/notebooks" -Recurse | Where-Object { $_.Name -ne 'empty.txt' } | Remove-Item -Force -Recurse
