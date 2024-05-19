# SQL examples

This project demonstrates basic SQL procedures in Java, Python, Node.js, and Go, using SQLite as the underlying database engine.

Use the provided Makefile to run the examples. There is no need to install SQLite separately.

- Run `installj.sh` prior to running the Java example for the first time

While this project uses SQLite, it's trivial to adapt this to relational database management systems (RDBMSs) like MySQL and PostgreSQL.

- All SQL databases are accessed with a driver
- While SQLite is an embedded database engine, MySQL and PostgreSQL are accessed via a server
    - The default MySQL port is 3306; the default PostgreSQL port is 5432
    - This is also how other database programs like MongoDB work, as well as in-memory data stores like Redis and Memcached
- Java and Go use driver-agnostic frameworks, which makes it easy to switch between SQLite, MySQL, and PostgreSQL drivers
- Python only has built-in support for SQLite
    - However, just changing `sqlite3` to either `mysql.connector` or `psycopg2` and adding additional arguments to the `connect` method (username and password for the server) allows you to easily use MySQL or PostgreSQL respectively, since the APIs are identical
    - You'll need to run a `CREATE DATABASE sample` command in lieu of opening a `sample.db` file
    - Otherwise you can specify that database in the `connect` method
- Node.js requires separate libraries for MySQL and PostgreSQL
- Since SQLite is written in C, the libsqlite3 library can be used
- To view the contents of a .db file from the command line, use `sqlite3`
- To view the contents of a .db file in a GUI, use DB Browser for SQLite
- To view the contents of a .db file in a web browser, use [sqlite-web](https://github.com/coleifer/sqlite-web)

This project uses simple SQL. More advanced projects may leverage SQL specific to a particular RDBMS. In those cases, there exist object-relational mapping (ORM) libraries that abstract away the actual SQL commands into RDBMS-independent function calls. Also, given their name, they let you _map_ an _object_ (instance of a Java/Python/JavaScript class or Go struct) to a row of a table in a _relational_ DBMS. Examples include:

- Hibernate (Java, technically Jakarta EE)
- SQLAlchemy (Python)
- Sequelize (Node.js)
- GORM (Go)
