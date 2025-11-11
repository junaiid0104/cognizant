-- Create Database
CREATE DATABASE bank_management;
USE bank_management;

-- Table: Customers
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15)
);

-- Table: Accounts
CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    account_type ENUM('Savings', 'Current') NOT NULL,
    balance DECIMAL(12,2) DEFAULT 0,
    interest_rate DECIMAL(5,2) DEFAULT 0, -- only used for savings
    overdraft_limit DECIMAL(12,2) DEFAULT 0, -- only used for current
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Table: Transactions
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT NOT NULL,
    type ENUM('Deposit', 'Withdrawal', 'Transfer') NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id)
);

-- ✅ Sample Data
INSERT INTO customers (name, email, phone)
VALUES ('Aisha Khan', 'aisha@example.com', '9990011111'),
       ('Rahul Verma', 'rahul@example.com', '8880022222');

INSERT INTO accounts (customer_id, account_type, balance, interest_rate)
VALUES (1, 'Savings', 5000, 3.5),
       (2, 'Current', 10000, 0);

INSERT INTO transactions (account_id, type, amount, note)
VALUES (1, 'Deposit', 5000, 'Initial Deposit'),
       (2, 'Deposit', 10000, 'Initial Deposit');
