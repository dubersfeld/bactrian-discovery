#!/bin/sh
echo "********************************************************"
echo "Starting Bactrian Eureka Server"
echo "********************************************************"

java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
