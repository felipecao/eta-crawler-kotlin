#!/bin/bash

echo "Updating code..."
cd ..
git pull

echo "Building JAR..."
./gradlew clean build -x test