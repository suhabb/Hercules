# Hercules
Spring Boot Apache Camel | Kafka Prject

## Mac

### Zookeeper Start Up
```
./zookeeper-server-start.sh ../config/zookeeper.properties
```
### Kafka Start Up
```
./kafka-server-start.sh ../config/server.properties
```

### Create Topic

- InputTopic
- ErrorTopic 
```
./kafka-topics.sh --create --topic inputItemTopic -zookeeper localhost:2181 --replication-factor 1 --partitions 3

./kafka-topics.sh --create --topic errorTopic -zookeeper localhost:2181 --replication-factor 1 --partitions 3
```

### Console Producer
```
./kafka-console-producer.sh --broker-list localhost:9092 --topic inputItemTopic
./kafka-console-producer.sh --broker-list localhost:9092 --topic errorTopic
```
### Console Consumer
```
./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic inputItemTopic --from-beginning
./kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic errorTopic --from-beginning

```
### PostgreSQL
```
 jdbc:postgresql://localhost:5432/localDB
 
 Table :  Items
```
