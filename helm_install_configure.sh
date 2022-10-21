#!/bin/sh
set -e

echo "Installing helm on your machine"
curl -LO https://git.io/get_helm.sh

# If script looks fine:
chmod 700 get_helm.sh
bash get_helm.sh
rm get_helm.sh

# Clone Helm Chart Repository
git clone https://github.com/CRC-901-On-the-Fly-Computing/Helm-Charts.git
cd helm-charts/


# Create a tiller user 

# Switch to the tiller files directory
cd tiller-files/

echo " Apply the tiller files"

kubectl apply -f tiller-user.yaml
kubectl apply -f tiller-cluster-role-binding.yaml

echo " Create tiller service account"
helm init --service-account tiller-user

echo "Add helm repos"
helm repo add stable https://charts.helm.sh/stable 
#helm repo add local http://127.0.0.1:8879/charts
helm repo add elastic https://helm.elastic.co
