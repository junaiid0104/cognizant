import uuid
from datetime import datetime


class Customer:
    def __init__(self, name, email, phone):
        self.customer_id = str(uuid.uuid4())
        self.name = name
        self.email = email
        self.phone = phone

    def __str__(self):
        return f"{self.name} ({self.customer_id}) - {self.email} - {self.phone}"


class Account:
    def __init__(self, customer_id, account_type, balance=0.0):
        self.account_id = str(uuid.uuid4())
        self.customer_id = customer_id
        self.account_type = account_type
        self.balance = balance
        self.created_at = datetime.now()

    def deposit(self, amount):
        if amount <= 0:
            raise ValueError("Deposit must be positive")
        self.balance += amount

    def withdraw(self, amount):
        if amount <= 0:
            raise ValueError("Withdrawal must be positive")
        if amount > self.balance:
            raise ValueError("Insufficient balance")
        self.balance -= amount

    def __str__(self):
        return f"{self.account_type} Account {self.account_id[:8]} | Balance: ₹{self.balance:.2f}"


class Transaction:
    def __init__(self, account_id, type_, amount, note=""):
        self.transaction_id = str(uuid.uuid4())
        self.account_id = account_id
        self.type = type_
        self.amount = amount
        self.note = note
        self.timestamp = datetime.now()

    def __str__(self):
        return f"[{self.timestamp}] {self.type} of ₹{self.amount} - {self.note}"


class Bank:
    def __init__(self):
        self.customers = {}
        self.accounts = {}
        self.transactions = []

    def create_customer(self, name, email, phone):
        customer = Customer(name, email, phone)
        self.customers[customer.customer_id] = customer
        print("✅ Customer created:", customer)
        return customer

    def create_account(self, customer_id, account_type, initial_deposit=0.0):
        if customer_id not in self.customers:
            raise ValueError("Customer not found")
        account = Account(customer_id, account_type, initial_deposit)
        self.accounts[account.account_id] = account
        if initial_deposit > 0:
            self.transactions.append(Transaction(account.account_id, "Deposit", initial_deposit, "Initial Deposit"))
        print("✅ Account created:", account)
        return account

    def deposit(self, account_id, amount):
        account = self.accounts.get(account_id)
        if not account:
            raise ValueError("Account not found")
        account.deposit(amount)
        self.transactions.append(Transaction(account_id, "Deposit", amount))
        print(f"Deposited ₹{amount} into {account.account_type} account.")

    def withdraw(self, account_id, amount):
        account = self.accounts.get(account_id)
        if not account:
            raise ValueError("Account not found")
        account.withdraw(amount)
        self.transactions.append(Transaction(account_id, "Withdrawal", amount))
        print(f"Withdrew ₹{amount} from {account.account_type} account.")

    def view_transactions(self, account_id):
        for t in self.transactions:
            if t.account_id == account_id:
                print(t)


# ---- Example Run ----
if __name__ == "__main__":
    bank = Bank()
    c1 = bank.create_customer("Aisha Khan", "aisha@example.com", "9990011111")
    a1 = bank.create_account(c1.customer_id, "Savings", 5000)

    bank.deposit(a1.account_id, 1000)
    bank.withdraw(a1.account_id, 500)

    print("\nTransactions:")
    bank.view_transactions(a1.account_id)
