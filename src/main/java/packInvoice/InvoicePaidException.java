package packInvoice;

public class InvoicePaidException extends Throwable {
    public InvoicePaidException(String message) {
        super(message);
    }
}
