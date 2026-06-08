import java.util.Scanner;
import java.util.HashMap;

// Represents a single bank account
class Account {
    private String accountNumber;
    private String pin;
    private String holderName;
    private double balance;

    public Account(String accountNumber, String pin, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.holderName = holderName;
        this.balance = balance;
    }

    public boolean validatePin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName()    { return holderName; }
    public double getBalance()       { return balance; }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false; // Insufficient funds
        }
        balance -= amount;
        return true;
    }
}

// Main ATM logic
public class ATMSystem {

    static HashMap<String, Account> accounts = new HashMap<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-load some demo accounts
        accounts.put("1001", new Account("1001", "1234", "Bharani", 15000.00));
        accounts.put("1002", new Account("1002", "5678", "Priya ", 8500.00));

        System.out.println("========================================");
        System.out.println("      Welcome to Coding Samurai ATM     ");
        System.out.println("========================================");

        // Step 1: Get account number
        System.out.print("Enter Account Number: ");
        String accNo = scanner.nextLine().trim();

        Account account = accounts.get(accNo);
        if (account == null) {
            System.out.println("Account not found. Exiting.");
            return;
        }

        // Step 2: Validate PIN (max 3 attempts)
        int attempts = 0;
        boolean authenticated = false;

        while (attempts < 3) {
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();

            if (account.validatePin(pin)) {
                authenticated = true;
                break;
            } else {
                attempts++;
                System.out.println("Wrong PIN. Attempts left: " + (3 - attempts));
            }
        }

        if (!authenticated) {
            System.out.println("Too many failed attempts. Card blocked.");
            return;
        }

        System.out.println("\nWelcome, " + account.getHolderName() + "!");

        // Step 3: Show menu
        boolean running = true;
        while (running) {
            System.out.println("\n-------- ATM Menu --------");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.printf("Current Balance: Rs. %.2f%n", account.getBalance());
                    break;

                case "2":
                    System.out.print("Enter deposit amount: Rs. ");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine().trim());
                        if (amount <= 0) {
                            System.out.println("Amount must be greater than zero.");
                        } else {
                            account.deposit(amount);
                            System.out.printf("Rs. %.2f deposited successfully.%n", amount);
                            System.out.printf("New Balance: Rs. %.2f%n", account.getBalance());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount entered.");
                    }
                    break;

                case "3":
                    System.out.print("Enter withdrawal amount: Rs. ");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine().trim());
                        if (amount <= 0) {
                            System.out.println("Amount must be greater than zero.");
                        } else if (account.withdraw(amount)) {
                            System.out.printf("Rs. %.2f withdrawn successfully.%n", amount);
                            System.out.printf("Remaining Balance: Rs. %.2f%n", account.getBalance());
                        } else {
                            System.out.println("Insufficient balance!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount entered.");
                    }
                    break;

                case "4":
                    System.out.println("Thank you for using Coding Samurai ATM. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please choose 1-4.");
            }
        }
        scanner.close();
    }
}
