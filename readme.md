### Request-response pattern example

_Inspired by https://reflectoring.io/amqp-request-response/_

- Spring Boot
- RabbitMQ
- Maven
- Java 11
- K6

#### To run:

1. Build
```commandline
mvn clean package
```

2. Run
```commandline
docker-compose up -d --build --scale request-service=3 --scale response-service=3
```

3. Test
```commandline
k6 run client/src/test/k6/load-test.js
```
4. Check (guest/guest): http://localhost:15672/#/queues/demo.queue

5. Stop
```commandline
docker-compose down
```