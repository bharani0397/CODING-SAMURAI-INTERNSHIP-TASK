import java.util.ArrayList;
import java.util.Scanner;

// Represents a single book
class Book {
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getId()           { return id; }
    public String getTitle()     { return title; }
    public String getAuthor()    { return author; }
    public boolean isAvailable() { return isAvailable; }

    public void borrowBook()  { isAvailable = false; }
    public void returnBook()  { isAvailable = true; }

    @Override
    public String toString() {
        return String.format("[ID: %d] \"%s\" by %s | Status: %s",
                id, title, author, isAvailable ? "Available" : "Borrowed");
    }
}

// Represents a library member
class Member {
    private int memberId;
    private String name;
    private ArrayList<Book> borrowedBooks;

    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    public int getMemberId()               { return memberId; }
    public String getName()                { return name; }
    public ArrayList<Book> getBorrowedBooks() { return borrowedBooks; }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
        book.borrowBook();
    }

    public boolean returnBook(int bookId) {
        for (Book book : borrowedBooks) {
            if (book.getId() == bookId) {
                borrowedBooks.remove(book);
                book.returnBook();
                return true;
            }
        }
        return false;
    }
}

// Main Library System
public class LibraryManagementSystem {

    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Member> members = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Pre-load demo books
        books.add(new Book(1, "Java: The Complete Reference", "Herbert Schildt"));
        books.add(new Book(2, "Clean Code", "Robert C. Martin"));
        books.add(new Book(3, "Head First Java", "Kathy Sierra"));
        books.add(new Book(4, "Effective Java", "Joshua Bloch"));
        books.add(new Book(5, "Data Structures in Java", "Robert Lafore"));

        // Pre-load demo members
        members.add(new Member(101, "Arjun Kumar"));
        members.add(new Member(102, "Priya Sharma"));

        System.out.println("============================================");
        System.out.println("   Welcome to Coding Samurai Library System  ");
        System.out.println("============================================");

        boolean running = true;
        while (running) {
            System.out.println("\n======== Main Menu ========");
            System.out.println("1. View All Books");
            System.out.println("2. Search Book by Title");
            System.out.println("3. Borrow a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. View Borrowing History (by Member)");
            System.out.println("6. Add New Book");
            System.out.println("7. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": viewAllBooks(); break;
                case "2": searchBook(); break;
                case "3": borrowBook(); break;
                case "4": returnBook(); break;
                case "5": viewMemberHistory(); break;
                case "6": addNewBook(); break;
                case "7":
                    System.out.println("Thank you for using the Library System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1-7.");
            }
        }
        scanner.close();
    }

    // Display all books
    static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // Search book by title keyword
    static void searchBook() {
        System.out.print("Enter title keyword to search: ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        boolean found = false;

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword)) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) System.out.println("No book found with that keyword.");
    }

    // Borrow a book
    static void borrowBook() {
        System.out.print("Enter your Member ID: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Member ID.");
            return;
        }

        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("Member not found. Please register first.");
            return;
        }

        viewAllBooks();
        System.out.print("Enter Book ID to borrow: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Book ID.");
            return;
        }

        Book book = findBook(bookId);
        if (book == null) {
            System.out.println("Book not found.");
        } else if (!book.isAvailable()) {
            System.out.println("Sorry, \"" + book.getTitle() + "\" is already borrowed.");
        } else {
            member.borrowBook(book);
            System.out.println("\"" + book.getTitle() + "\" borrowed successfully by " + member.getName() + "!");
        }
    }

    // Return a book
    static void returnBook() {
        System.out.print("Enter your Member ID: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Member ID.");
            return;
        }

        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        if (member.getBorrowedBooks().isEmpty()) {
            System.out.println(member.getName() + " has no borrowed books.");
            return;
        }

        System.out.println("Books borrowed by " + member.getName() + ":");
        for (Book b : member.getBorrowedBooks()) {
            System.out.println("  " + b);
        }

        System.out.print("Enter Book ID to return: ");
        int bookId;
        try {
            bookId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Book ID.");
            return;
        }

        if (member.returnBook(bookId)) {
            System.out.println("Book returned successfully. Thank you!");
        } else {
            System.out.println("That book was not borrowed by this member.");
        }
    }

    // View member's borrowing history
    static void viewMemberHistory() {
        System.out.print("Enter Member ID: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Member ID.");
            return;
        }

        Member member = findMember(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        System.out.println("\nCurrently borrowed by " + member.getName() + ":");
        if (member.getBorrowedBooks().isEmpty()) {
            System.out.println("  No books currently borrowed.");
        } else {
            for (Book b : member.getBorrowedBooks()) {
                System.out.println("  " + b);
            }
        }
    }

    // Add a new book to the library
    static void addNewBook() {
        System.out.print("Enter Book Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Author Name: ");
        String author = scanner.nextLine().trim();

        int newId = books.size() + 1;
        books.add(new Book(newId, title, author));
        System.out.println("Book \"" + title + "\" added successfully with ID: " + newId);
    }

    // Helper: find member by ID
    static Member findMember(int id) {
        for (Member m : members) {
            if (m.getMemberId() == id) return m;
        }
        return null;
    }

    // Helper: find book by ID
    static Book findBook(int id) {
        for (Book b : books) {
            if (b.getId() == id) return b;
        }
        return null;
    }
}
