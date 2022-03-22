#!/bin/bash
echo "Stopping Kafka Sagas"

echo "Deleting Kubernetes Sagas"
kubectl delete -f kubernetes/sagas

echo "Deleting Kubernetes Kafka"
kubectl delete -f kubernetes/kafka

echo "Stopping minikube"
minikube stop

echo "deleting minikube"
minikube delete
