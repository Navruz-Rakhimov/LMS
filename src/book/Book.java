package book;

import java.util.Set;

// will be saved in table books;
public class Book {

    private String isbn;
    private String title;
    private String edition;
    private String copyright;
    int quantity;

    public Book(String isbn, String title, String edition, String copyright, int quantity) {
        this.isbn = isbn;
        this.title = title;
        this.edition = edition;
        this.copyright = copyright;
        this.quantity = quantity;
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
}
