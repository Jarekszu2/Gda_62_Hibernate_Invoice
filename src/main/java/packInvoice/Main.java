package packInvoice;

/*
Rachunek (Invoice) ma pola:
- dataDodania
- nazwaKlienta
- czyOpłacony
- dataWydania
- dataIGodzinaOpłacenia
- kwotaNaRachunku (suma wartości produktów)
- zbiór produktów (relacja bazodanowa - osobna tabela)
Produkt na fakturze (InvoicePosition/Position) ma pola:
- nazwa
- cena (netto)
- kwota podatku
- ilość (int)
Możliwości aplikacji:
Stwórz metody które pozwalają na:
- dodawanie rachunku
- dodawanie produktów (do rachunku)
- ustawianie rachunku jako opłaconego (po opłaceniu rachunku nie powinna istnieć możliwość dodawania produktów)
- sprawdzanie kwoty rachunku po identyfikatorze
- listowanie produktów na rachunku
- listowanie rachunków
- listowanie wszystkich produktów
- listowanie rachunków nieopłaconych
- listowanie rachunków z ostatniego tygodnia
- wypisywanie sumy kwot rachunku z obecnego dnia.
 */


import java.util.List;

public class Main {


    public static void main(final String[] args) throws Exception {
        System.out.println();
        ScannerWork scannerWork = new ScannerWork();
        InvoiceDao invoiceDao = new InvoiceDao();
        InvoicePositionDao invoicePositionDao = new InvoicePositionDao();

        System.out.println("Aplikacja wspomaga pracę z fakturami w sklepie.");
        System.out.println();
        boolean flag = false;
        do {
            System.out.println();
            System.out.println("Wybierz:\n a) insert Invoice\n b) insert InvoicePosition\n c) select all Invoices" +
                    "\n d) select all InvoicePositions\n e) set invoice 'paid'\n f) select positions for invoiceId" +
                    "\n g) select not paid invoices\n h) print invoices from last week\n i) check invoice amount by invoiceId" +
                    "\n j) print sum of present day invoices amounts\n k) getListPositionsByInvoiceId\n l) L" +
                    "\n m) M\n w) quit");
            char znak = scannerWork.getChar();
            switch (znak) {
                case 'a':
                    scannerWork.buildInvoice_mainA();
                    break;
                case 'b':
                    scannerWork.buildInvoicePosition_mainB();
                    break;
                case 'c':
                    invoiceDao.printAllInvoicesVer2_mainC();
                    break;
                case 'd':
                    invoicePositionDao.printAllInvoicePositions();
                    break;
                case 'e':
                    scannerWork.setInvoicePaid_mainE();
                    break;
                case 'f':
                    Long invoiceIdF = scannerWork.getId();
                    invoicePositionDao.printAllInvoicePositionForInvoiceId(invoiceIdF);
//                    scannerWork.printPositionsForInvoiceId();
                    break;
                case 'g':
                    List<Invoice> invoiceListG = invoiceDao.getAllUnpaid();
                    invoiceListG.forEach(System.err::println);
//                    scannerWork.printNotPaidInvoices();
                    break;
                case 'h':
                    List<Invoice> invoiceListH = invoiceDao.getAllFromLastWeek();
                    invoiceListH.forEach(System.err::println);
//                    scannerWork.printInvoicesFromLastWeek();
                    break;
                case 'i':
                    Long invoiceIdI = scannerWork.getId();
                    Double amount = invoiceDao.getAmountFromInvoiceId(invoiceIdI);
                    System.err.println("Amount of invoice number " + invoiceIdI + ": " + amount);
//                    scannerWork.checkInvoiceAmountByInvoiceId();
                    break;
                case 'j':
                    invoiceDao.getSumInvoicesOfPresentDay();
//                    scannerWork.printSumOfInvoicesAmountsFromPresenDay();
                    break;
                case 'k':
                    Long invoiceIdK = scannerWork.getId();
                    List<String> stringListK = invoicePositionDao.getPositionsWhereInvoiceId(invoiceIdK);
                    stringListK.forEach(System.err::println);
                    break;
                case 'l':
                    System.out.println("L");
                    break;
                case 'm':
                    System.out.println("M");
                    break;
                case 'w':
                    flag = true;
            }
        } while (!flag);
    }
}