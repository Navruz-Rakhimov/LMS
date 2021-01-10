package book;

public class BorrowedBook {
    private String isbn;
    private String title;
    private int borrowedAmount;

    BorrowedBook(String isbn, String title, int amount){
        this.isbn = isbn;
        this.title = title;
        this.borrowedAmount = amount;
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

    public int getBorrowedAmount() {
        return borrowedAmount;
    }

    public void resetAmount(){
        this.borrowedAmount = 1;
    }

    public void incrementAmount() {
        this.borrowedAmount += 1;
    }
}
