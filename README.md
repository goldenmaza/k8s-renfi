# Renfi - Cloud Native Edition

## Overview
The goal of this project is to modernize the Renfi application by migrating it to Java 25. This migration focuses
on leveraging the latest language features to improve performance and maintainability, while transitioning from
a standalone architecture to a distributed microservices architecture deployed on Kubernetes.

## Architecture & Modules
The project is structured as a multi-module system to ensure a clean separation of concerns.

1) Instructor module: The orchestrator of the system. It exposes a REST API (OpenAPI 3.1.1) to receive instructions and manages the workflow by producing events to a Kafka cluster.
2) Handler module: The worker node. It consumes instructions from Kafka and performs the heavy lifting of modifying files stored in a shared Kubernetes volume.
3) Shared module: A common library containing shared Data Transfer Objects (DTOs), database entities, and repository interfaces to ensure type safety and consistency across services.
4) Infrastructure (k8s) directory: A dedicated directory containing the Kubernetes manifests and Helm charts required to deploy and scale the services in a cluster.
5) Frontend module: A modern user interface that allows users to submit instructions and monitor the processing status of their files.

### Instructor Application
TODO: describe the API and orchestration logic

### Handler Application
TODO: describe the file processing and storage logic

### Shared Module
TODO: describe the shared entities and data contracts

### Infrastructure Directory
TODO: describe the Helm charts and the Shared Volume strategy

### Vue Application
TODO: describe the UI/UX features
