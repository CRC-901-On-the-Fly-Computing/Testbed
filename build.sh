#!/bin/bash

# Get the docker respository details from the user
read -p "Enter base URL of your docker registry (User Nexus URL without protocol): " nexus_base_url
read -p "Enter the docker repository name : " docker_repository_name

read -p "Please provide the master node name or IP of the kubernetes cluster:" master_ip
sed -i "s|localhost|$master_ip|g" website/WebContent/src/app/app.url.json

escaped_nexus_base_url=$(printf '%s\n' "$nexus_base_url" | sed -e 's/[\/&]/\\&/g')
escaped_docker_repository_name=$(printf '%s\n' "$docker_repository_name" | sed -e 's/[\/&]/\\&/g')

projects=("service_requester" "review_board" "system_manager" "otfp_registry_new" "credential_issuer" "buy_processor" "composition_analysis" "proseco_configurator" "policy_provider" "kubernetes_executor_spawner")

# shellcheck disable=SC2068
for project in ${projects[@]}; do
    echo 'Building ' ${project}
    ./gradlew :${project}:build -x test :${project}:docker :${project}:dockerTag :${project}:dockerTagsPush
    echo 'Build successfull ' ${project}
done

echo "Building sede proxy"
cd execution-gateway/sede_proxy
echo "Switched to sede proxy directory and building docker image"
docker build -t $escaped_nexus_base_url/$escaped_docker_repository_name/sede.proxy:latest .
docker push $escaped_nexus_base_url/$escaped_docker_repository_name/sede.proxy:latest
echo "Sede proxy Build successfull"
cd ../..

echo "Building Website"
cd website/WebContent
echo "Switch to webcontent directory"
npm install && ng build
cd ..
echo "Build docker image"
docker build -t $escaped_nexus_base_url/$escaped_docker_repository_name/website:latest .
docker push $escaped_nexus_base_url/$escaped_docker_repository_name/website:latest
echo "Website Build successfull"
