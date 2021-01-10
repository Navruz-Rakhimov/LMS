package book;

public class Fine {
    String isbn;
    double fineAmount;

    public Fine(String isbn, double fineAmount) {
        this.isbn = isbn;
        this.fineAmount = fineAmount;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
