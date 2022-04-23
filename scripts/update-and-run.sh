#!/bin/bash

echo "Updating code..."
cd ..
git pull

echo "Building JAR..."
./gradlew clean build -x test

echo "Running app now!"
java -jar "build/libs/ETA Crawler-0.0.1-SNAPSHOT.jar"