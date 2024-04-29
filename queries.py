import sqlite3
import datetime

conn = sqlite3.connect("sample.db")
cur = conn.cursor()

cur.execute("DROP TABLE IF EXISTS customer_orders")
cur.execute("DROP TABLE IF EXISTS customers")

cur.execute("CREATE TABLE customers " +
                "(customer_id INTEGER PRIMARY KEY, " + 
                "name VARCHAR(50) NOT NULL, " +
                "email_address VARCHAR(50) NOT NULL)")
cur.execute("CREATE TABLE customer_orders " +
                "(customer_order_id INTEGER PRIMARY KEY, " +
                "customer_id INTEGER, " +
                "amount FLOAT NOT NULL, " +
                "order_date TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))")

cur.execute("INSERT INTO customers (name, email_address) VALUES ('Charlie', 'charlie@gmail.com')")
conn.commit()
if cur.rowcount != 1:
    print("INSERT Charlie failed")

cur.execute("INSERT INTO customers (name, email_address) VALUES ('Bob', 'bob@gmail.com')")
conn.commit()
if cur.rowcount != 1:
    print("INSERT Bob failed")

cur.execute("INSERT INTO customers (name, email_address) VALUES (?, ?)", ("Alice", "alice@outlook.com"))
conn.commit()
if cur.rowcount != 1:
    print("INSERT Alice failed")

res = cur.execute("SELECT customer_id FROM customers WHERE email_address = 'bob@gmail.com'")
customer_id, = res.fetchone()
cur.execute("INSERT INTO customer_orders (customer_id, amount, order_date) VALUES (?, ?, ?)", (customer_id, 13.95, datetime.datetime.now()))
conn.commit()
if cur.rowcount != 1:
    print("INSERT Bob order failed")

res = cur.execute("SELECT customer_id FROM customers WHERE email_address = 'alice@outlook.com'")
customer_id, = res.fetchone()
cur.execute("UPDATE customers SET email_address = 'alice@gmail.com' WHERE customer_id = ?", (customer_id,))
conn.commit()
if cur.rowcount != 1:
    print("UPDATE Alice failed")

cur.execute("DELETE FROM customers WHERE email_address = 'charlie@gmail.com'")
conn.commit()
if cur.rowcount != 1:
    print("DELETE Charlie failed")

res = cur.execute("SELECT * FROM customers")
rows = res.fetchall()
print("customers:")
for row in rows:
    print("|".join([str(x) for x in row]))

res = cur.execute("SELECT * FROM customer_orders")
rows = res.fetchall()
print("customer_orders:")
for row in rows:
    print("|".join([str(x) for x in row]))

conn.close()
