// "JDBC" refers to java.sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Queries {
    public static void main(String[] args) throws SQLException {
        Connection conn;
        Statement stmt;
        PreparedStatement pstmt;
        String cmd;
        int rowCount, customerId;
        ResultSet rs;

        conn = DriverManager.getConnection("jdbc:sqlite:sample.db");
        stmt = conn.createStatement();

        cmd = "DROP TABLE IF EXISTS customer_orders";
        stmt.executeUpdate(cmd);
        cmd = "DROP TABLE IF EXISTS customers";
        stmt.executeUpdate(cmd);

        cmd = "CREATE TABLE customers " +
                "(customer_id INTEGER PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "email_address VARCHAR(50) NOT NULL)";
        stmt.executeUpdate(cmd);
        cmd = "CREATE TABLE customer_orders " +
                "(customer_order_id INTEGER PRIMARY KEY, " +
                "customer_id INTEGER, " +
                "amount FLOAT NOT NULL, " +
                "order_date TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (customer_id) REFERENCES customers(customer_id))";
        stmt.executeUpdate(cmd);

        cmd = "INSERT INTO customers (name, email_address) VALUES ('Charlie', 'charlie@gmail.com')";
        rowCount = stmt.executeUpdate(cmd);
        if (rowCount != 1) {
            System.out.println("INSERT Charlie failed");
        }

        cmd = "INSERT INTO customers (name, email_address) VALUES ('Bob', 'bob@gmail.com')";
        rowCount = stmt.executeUpdate(cmd);
        if (rowCount != 1) {
            System.out.println("INSERT Bob failed");
        }

        cmd = "INSERT INTO customers (name, email_address) VALUES (?, ?)";
        pstmt = conn.prepareStatement(cmd);
        pstmt.setString(1, "Alice");
        pstmt.setString(2, "alice@outlook.com");
        rowCount = pstmt.executeUpdate();
        if (rowCount != 1) {
            System.out.println("INSERT Alice failed");
        }

        String[][] data = {{"Daniel", "daniel@gmail.com"}, {"Frank", "frank@gmail.com"}};
        conn.setAutoCommit(false);
        pstmt = conn.prepareStatement(cmd);
        for (int i = 0; i < data.length; i++) {
            pstmt.setString(1, data[i][0]);
            pstmt.setString(2, data[i][1]);
            pstmt.addBatch();
        }
        try {
            pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            System.out.println("INSERT Daniel and Frank failed");
        }
        pstmt.close();
        conn.setAutoCommit(true);

        // SELECT FIRST customer_id ... retrieves the first result only
        // SELECT DISTINCT name ... retrieves the first row for each distinct name
        // ... WHERE email_address LIKE 'bob@%' retrieves all rows with email
        // addresses starting with "bob@"
        cmd = "SELECT customer_id FROM customers WHERE email_address = 'bob@gmail.com'";
        rs = stmt.executeQuery(cmd);
        rs.next();
        customerId = rs.getInt("customer_id");
        cmd = "INSERT INTO customer_orders (customer_id, amount, order_date) VALUES (?, ?, ?)";
        pstmt = conn.prepareStatement(cmd);
        pstmt.setInt(1, customerId);
        pstmt.setFloat(2, 13.95f);
        pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        rowCount = pstmt.executeUpdate();
        if (rowCount != 1) {
            System.out.println("INSERT Bob order failed");
        }

        cmd = "SELECT customer_id FROM customers WHERE email_address = 'alice@outlook.com'";
        rs = stmt.executeQuery(cmd);
        rs.next();
        customerId = rs.getInt("customer_id");
        cmd = "UPDATE customers SET email_address = 'alice@gmail.com' WHERE customer_id = ?";
        pstmt = conn.prepareStatement(cmd);
        pstmt.setInt(1, customerId);
        rowCount = pstmt.executeUpdate();
        if (rowCount != 1) {
            System.out.println("UPDATE Alice failed");
        }

        cmd = "DELETE FROM customers WHERE email_address = 'charlie@gmail.com'";
        rowCount = stmt.executeUpdate(cmd);
        if (rowCount != 1) {
            System.out.println("DELETE Charlie failed");
        }

        // add DESC to the end of the query to sort in reverse order
        cmd = "SELECT * FROM customers ORDER BY name";
        rs = stmt.executeQuery(cmd);
        System.out.println("customers:");
        while (rs.next()) {
            System.out.println(
                rs.getInt("customer_id") + "|" +
                rs.getString("name") + "|" +
                rs.getString("email_address")
            );
        }

        cmd = "SELECT COUNT(*) as total FROM customers";
        rs = stmt.executeQuery(cmd);
        rs.next();
        System.out.println(rs.getInt("total"));

        cmd = "SELECT * FROM customer_orders";
        rs = stmt.executeQuery(cmd);
        System.out.println("customer_orders:");
        while (rs.next()) {
            System.out.println(
                rs.getInt("customer_order_id") + "|" +
                rs.getInt("customer_id") + "|" +
                rs.getFloat("amount") + "|" +
                rs.getTimestamp("order_date")
            );
        }

        conn.close();
    }
}
