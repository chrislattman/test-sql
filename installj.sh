#!/bin/bash

curl -LO https://repo.maven.apache.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar
curl -LO https://repo.maven.apache.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar
mkdir lib
mv ./*.jar lib
