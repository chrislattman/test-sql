# SQL examples

This project demonstrates basic SQL procedures in Java, Python, Node.js, and Go, using SQLite as the underlying database engine.

Use the provided Makefile to run the examples. There is no need to install SQLite separately.

- Run `installj.sh` prior to running the Java example for the first time

While this project uses SQLite, it's trivial to adapt this to RDBMSes like MySQL and PostgreSQL.

- All SQL databases are accessed with a driver
- While SQLite is an embedded database engine, MySQL and PostgreSQL are accessed via a server
    - The default MySQL port is 3306; the default PostgreSQL port is 5432
    - This is also how other database engines like MongoDB work
- Java and Go use driver-agnostic frameworks, which makes it easy to switch between SQLite, MySQL, and PostgreSQL drivers
- Python only has built-in support for SQLite
    - However, just changing `sqlite3` to either `mysql.connector` or `psycopg2` and adding additional arguments to the `connect` method (username and password for the server) allows you to easily use MySQL or PostgreSQL respectively, since the APIs are identical
    - You'll need to run a `CREATE DATABASE sample` command in lieu of opening a `sample.db` file
    - Otherwise you can specify that database in the `connect` method
- Node.js requires separate libraries for MySQL and PostgreSQL

This project uses simple SQL. More advanced projects may leverage SQL specific to a particular RDBMS. In those cases, there exist object-relational mapping (ORM) libraries that abstract away the actual SQL commands into RDBMS-independent function calls. Examples include:

- Hibernate (Java)
- SQLAlchemy (Python)
- Sequelize (Node.js)
- GORM (Go)
