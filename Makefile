java:
	javac Queries.java
	java -cp .:lib/* Queries

python:
	python3 queries.py

nodejs:
	node queries.js

go:
	go run queries.go

clean:
	rm -rf *.class *.db
