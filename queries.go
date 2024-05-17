package main

import (
	"database/sql"
	"fmt"
	"log"
	"time"

	_ "github.com/mattn/go-sqlite3"
)

type Customer struct {
	CustomerID   int32
	Name         string
	EmailAddress string
}

type CustomerOrder struct {
	CustomerOrderID int32
	CustomerID      int32
	Amount          float32
	OrderDate       string
}

func main() {
	db, err := sql.Open("sqlite3", "sample.db")
	if err != nil {
		log.Fatal(err)
	}
	defer db.Close()

	_, err = db.Exec("DROP TABLE IF EXISTS customer_orders")
	if err != nil {
		log.Fatal(err)
	}
	_, err = db.Exec("DROP TABLE IF EXISTS customers")
	if err != nil {
		log.Fatal(err)
	}

	_, err = db.Exec("CREATE TABLE customers " +
		"(customer_id INTEGER PRIMARY KEY, " +
		"name VARCHAR(50) NOT NULL, " +
		"email_address VARCHAR(50) NOT NULL)")
	if err != nil {
		log.Fatal(err)
	}
	_, err = db.Exec("CREATE TABLE customer_orders " +
		"(customer_order_id INTEGER PRIMARY KEY, " +
		"customer_id INTEGER, " +
		"amount FLOAT NOT NULL, " +
		"order_date TIMESTAMP NOT NULL, " +
		"FOREIGN KEY (customer_id) REFERENCES customers(customer_id))")
	if err != nil {
		log.Fatal(err)
	}

	res, err := db.Exec("INSERT INTO customers (name, email_address) VALUES ('Charlie', 'charlie@gmail.com')")
	if err != nil {
		log.Fatal(err)
	}
	rowCount, _ := res.RowsAffected()
	if rowCount != 1 {
		log.Fatalln("INSERT Charlie failed")
	}

	res, err = db.Exec("INSERT INTO customers (name, email_address) VALUES ('Bob', 'bob@gmail.com')")
	if err != nil {
		log.Fatal(err)
	}
	rowCount, _ = res.RowsAffected()
	if rowCount != 1 {
		log.Fatalln("INSERT Bob failed")
	}

	res, err = db.Exec("INSERT INTO customers (name, email_address) VALUES (?, ?)", "Alice", "alice@outlook.com")
	if err != nil {
		log.Fatal(err)
	}
	rowCount, _ = res.RowsAffected()
	if rowCount != 1 {
		log.Fatalln("INSERT Alice failed")
	}

	row := db.QueryRow("SELECT customer_id FROM customers WHERE email_address = 'bob@gmail.com'")
	var customerId int
	err = row.Scan(&customerId)
	if err != nil {
		log.Fatal(err)
	}
	pstmt, err := db.Prepare("INSERT INTO customer_orders (customer_id, amount, order_date) VALUES (?, ?, ?)")
	if err != nil {
		log.Fatal(err)
	}
	defer pstmt.Close()
	res, err = pstmt.Exec(customerId, 13.95, time.Now().Format("2006-01-02 15:04:05"))
	if err != nil {
		log.Fatal(err)
	}
	rowCount, _ = res.RowsAffected()
	if rowCount != 1 {
		log.Fatalln("INSERT Bob order failed")
	}

	row = db.QueryRow("SELECT customer_id FROM customers WHERE email_address = 'alice@outlook.com'")
	err = row.Scan(&customerId)
	if err != nil {
		log.Fatal(err)
	}
	pstmt2, err := db.Prepare("UPDATE customers SET email_address = 'alice@gmail.com' WHERE customer_id = ?")
	if err != nil {
		log.Fatal(err)
	}
	defer pstmt2.Close()
	res, err = pstmt2.Exec(customerId)
	if err != nil {
		log.Fatal(err)
	}
	rowCount, _ = res.RowsAffected()
	if rowCount != 1 {
		log.Fatalln("UPDATE Alice failed")
	}

	res, err = db.Exec("DELETE FROM customers WHERE email_address = 'charlie@gmail.com'")
	if err != nil {
		log.Fatal(err)
	}
	rowCount, _ = res.RowsAffected()
	if rowCount != 1 {
		log.Fatalln("DELETE Charlie failed")
	}

	rows, err := db.Query("SELECT * FROM customers ORDER BY name")
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()
	fmt.Println("customers:")
	for rows.Next() {
		var customer Customer
		err = rows.Scan(
			&customer.CustomerID,
			&customer.Name,
			&customer.EmailAddress)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Println(
			fmt.Sprint(customer.CustomerID) + "|" +
				customer.Name + "|" +
				customer.EmailAddress)
	}

	rows2, err := db.Query("SELECT * FROM customer_orders")
	if err != nil {
		log.Fatal(err)
	}
	defer rows2.Close()
	fmt.Println("customers:")
	for rows2.Next() {
		var customerOrder CustomerOrder
		err = rows2.Scan(
			&customerOrder.CustomerOrderID,
			&customerOrder.CustomerID,
			&customerOrder.Amount,
			&customerOrder.OrderDate)
		if err != nil {
			log.Fatal(err)
		}
		fmt.Println(
			fmt.Sprint(customerOrder.CustomerOrderID) + "|" +
				fmt.Sprint(customerOrder.CustomerID) + "|" +
				fmt.Sprintf("%.2f", customerOrder.Amount) + "|" +
				customerOrder.OrderDate)
	}
}
