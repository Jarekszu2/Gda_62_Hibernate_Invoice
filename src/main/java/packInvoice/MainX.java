package packInvoice;

import java.time.LocalDate;

public class MainX {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        System.out.println(localDate);
    }
}
