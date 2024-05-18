const db = require("better-sqlite3")("sample.db");
db.pragma("journal_mode = WAL");

db.prepare("DROP TABLE IF EXISTS customer_orders").run();
db.prepare("DROP TABLE IF EXISTS customers").run();

db.prepare("CREATE TABLE customers " +
            "(customer_id INTEGER PRIMARY KEY, " +
            "name VARCHAR(50) NOT NULL, " +
            "email_address VARCHAR(50) NOT NULL)").run();
db.prepare("CREATE TABLE customer_orders " +
            "(customer_order_id INTEGER PRIMARY KEY, " +
            "customer_id INTEGER, " +
            "amount FLOAT NOT NULL, " +
            "order_date TIMESTAMP NOT NULL, " +
            "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))").run();

let info = db.prepare("INSERT INTO customers (name, email_address) VALUES ('Charlie', 'charlie@gmail.com')").run();
if (info.changes !== 1) {
    console.log("INSERT Charlie failed");
}

info = db.prepare("INSERT INTO customers (name, email_address) VALUES ('Bob', 'bob@gmail.com')").run();
if (info.changes !== 1) {
    console.log("INSERT Bob failed");
}

info = db.prepare("INSERT INTO customers (name, email_address) VALUES (?, ?)").run("Alice", "alice@outlook.com");
if (info.changes !== 1) {
    console.log("INSERT Alice failed");
}

let row = db.prepare("SELECT customer_id FROM customers WHERE email_address = 'bob@gmail.com'").get();
info = db.prepare("INSERT INTO customer_orders (customer_id, amount, order_date) VALUES (?, ?, ?)")
         .run(row.customer_id, 13.95, new Date().toISOString());
if (info.changes !== 1) {
    console.log("INSERT Bob order failed");
}

row = db.prepare("SELECT customer_id FROM customers WHERE email_address = 'alice@outlook.com'").get();
info = db.prepare("UPDATE customers SET email_address = 'alice@gmail.com' WHERE customer_id = ?")
         .run(row.customer_id);
if (info.changes !== 1) {
    console.log("UPDATE Alice failed");
}

info = db.prepare("DELETE FROM customers WHERE email_address = 'charlie@gmail.com'").run();
if (info.changes !== 1) {
    console.log("DELETE Charlie failed");
}

let rows = db.prepare("SELECT * FROM customers ORDER BY name").all();
console.log("customers:");
rows.forEach((row) => {
    console.log(
        row.customer_id + "|" +
        row.name + "|" +
        row.email_address
    );
});

row = db.prepare("SELECT COUNT(*) as total FROM customers").get();
console.log(row.total);

rows = db.prepare("SELECT * FROM customer_orders").all();
console.log("customer_orders:");
rows.forEach((row) => {
    console.log(
        row.customer_order_id + "|" +
        row.customer_id + "|" +
        row.amount + "|" +
        row.order_date
    );
});

db.close();
