В образе confluentinc/cp-kafka:latest Kafka перешёл на KRaft (без Zookeeper), а мы запускали с Zookeeper.

Нужно было добавить переменные для Kraft-режима:

KAFKA_PROCESS_ROLES: broker

KAFKA_NODE_ID: 1

CLUSTER_ID

KAFKA_CONTROLLER_QUORUM_VOTERS

KAFKA_CONTROLLER_LISTENER_NAMES

Без них Kafka не стартовал. Обычная проблема с новыми версиями образа.


MinioConfig — создаёт бин MinioClient с настройками из application.yml

MinioService — два метода: downloadFile(path) → byte[] и uploadFile(path, byte[])