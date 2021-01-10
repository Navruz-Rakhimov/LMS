package book;

import java.time.LocalDate;

// It is not just a book. It is a library book that may have values assigned to borrowed/dueDate if someone borrows it
// and fine associated with it if the loan period expires
public class Book {

    private String isbn;
    private String title;
    private String edition;
    private String copyright;

    int quantity;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private String fineAmount;

    public Book(String isbn, String title, String edition, String copyright, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.edition = edition;
        this.copyright = copyright;
        this.quantity = quantity;

        borrowedDate = null;
        dueDate = null;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", edition='" + edition + '\'' +
                ", copyright='" + copyright + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(String fineAmount) {
        this.fineAmount = fineAmount;
    }

}
