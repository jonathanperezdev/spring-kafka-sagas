#!/bin/bash
echo "Starting Kafka Sagas"

echo "Starting minikube"
minikube start --driver=docker --memory=6144 --cpus=4

echo "Compiling and building Spring projects"
sudo ./mvnw clean install

echo "Deploying order-service to docker hub"
sudo ./mvnw -pl order-service/ compile jib:build

echo "Deploying payment-service to docker hub"
sudo ./mvnw -pl payment-service/ compile jib:build

echo "Deploying stock-service to docker hub"
sudo ./mvnw -pl stock-service/ compile jib:build

echo "Starting Kubernetes Kafka"
kubectl apply -f kubernetes/kafka

echo "Starting Kubernetes Sagas"
kubectl apply -f kubernetes/sagas

echo "Setting the context for sagas name space"
kubectl config set-context --current --namespace=sagas-namespace