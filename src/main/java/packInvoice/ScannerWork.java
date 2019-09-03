package packInvoice;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class ScannerWork {
    Scanner scanner = new Scanner(System.in);
    EntityDao entityDao = new EntityDao();

    public char getChar() {
        char znak = 'a';
        znak = scanner.nextLine().charAt(0);
        return znak;
    }

    public Long getId() {
        System.out.println("Get id:");
        Long id = Long.valueOf(scanner.nextLine());
        return id;
    }

    public void buildInvoice_mainA() {
        Invoice invoice = new Invoice();

//        boolean paid = false;
//        invoice.setPaid(paid);
        LocalDateTime dateRelased = null;
        invoice.setDateRelased(dateRelased);
        LocalDateTime dateOfPayment = null;
        invoice.setDateOfPayment(dateOfPayment);

        entityDao.saveOrUpdate(invoice);
        System.err.println("Invoice added.");
    }

    public void buildInvoicePosition_mainB() {
        System.out.println("Get id of invoice:");
        Long invoiceId = Long.valueOf(scanner.nextLine());
        Optional<Invoice> optionalInvoice = entityDao.getById(Invoice.class, invoiceId);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            if (!invoice.isPaid()) {
                InvoicePosition invoicePosition = new InvoicePosition();

                invoicePosition.setInvoice(invoice);
                System.out.println("Get name of invoice position:");
                String positionName = scanner.nextLine();
                invoicePosition.setPositionName(positionName);
                System.out.println("Get quantity of position on the invoice:");
                int positionQuantity = Integer.parseInt((scanner.nextLine()));
                invoicePosition.setPositionQuantity(positionQuantity);
                System.out.println("Get amount of position on the invoice:");
                double positionAmount = Double.parseDouble((scanner.nextLine()));
                invoicePosition.setPositionAmount(positionAmount);
                System.out.println("Get tax rate (0.05, 0.07, 0.23):");
                double taxRate = Double.parseDouble((scanner.nextLine()));
                invoicePosition.setTaxRate(taxRate);

                entityDao.saveOrUpdate(invoicePosition);
                System.err.println("Invoice position added.");
            } else {
                try {
                    throw new InvoicePaidException("Invoice paid. Can not add any position!");
                } catch (InvoicePaidException e) {
                    System.err.println(e.getMessage());
                }
            }

        }
    }

    public void setInvoicePaid_mainE() {
        System.out.println("Get id of invoice:");
        Long invoiceId = Long.valueOf(scanner.nextLine());
        Optional<Invoice> optionalInvoice = entityDao.getById(Invoice.class, invoiceId);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setPaid(true);
            LocalDateTime localDateTime = LocalDateTime.now();
            invoice.setDateOfPayment(localDateTime);
            invoice.setDateRelased(localDateTime);

            entityDao.saveOrUpdate(invoice);
            System.err.println("Invoice paid.");
        }
    }

    public void printPositionsForInvoiceId() {
        System.out.println("Get id of invoice:");
        Long invoiceId = Long.valueOf(scanner.nextLine());
        Optional<Invoice> optionalInvoice = entityDao.getById(Invoice.class, invoiceId);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            Set<InvoicePosition> invoicePositionSet = invoice.getInvoicePositionSet();
            invoicePositionSet.forEach(System.out::println);
        }
    }

    public void printNotPaidInvoices() {
        List<Invoice> invoiceList = entityDao.getAll(Invoice.class);
        invoiceList.stream()
                .filter(invoice -> !invoice.isPaid())
                .forEach(System.out::println);
    }

    public void printInvoicesFromLastWeek() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        LocalDateTime ldtOneWeekAgo = localDateTime.minusDays(7L);
        LocalDate ldOneWeekAgo = ldtOneWeekAgo.toLocalDate();
        System.err.println("Range of dates: from " + ldOneWeekAgo + " to " + localDate + ".");
        List<Invoice> invoiceList = entityDao.getAll(Invoice.class);
        invoiceList.stream()
                .filter(invoice -> invoice.getDateAdded().isAfter(ldtOneWeekAgo))
                .forEach(System.out::println);
    }

    public void checkInvoiceAmountByInvoiceId() {
        System.out.println("Get id of invoice:");
        Long invoiceId = Long.valueOf(scanner.nextLine());
        Optional<Invoice> optionalInvoice = entityDao.getById(Invoice.class, invoiceId);

        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            double amount = invoice.getAmount();
            System.err.println("Amount of invoice with id " + invoiceId + ": " + amount);
        }
    }

    public void printSumOfInvoicesAmountsFromPresenDay() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate localDate = localDateTime.toLocalDate();
        LocalDateTime startOfPresentDay = localDate.atStartOfDay();
//        LocalDateTime ldtOneWeekAgo = localDateTime.minusDays(7L);
//        LocalDate ldOneWeekAgo = ldtOneWeekAgo.toLocalDate();
        System.err.println("Range of time: from " + startOfPresentDay + " to " + localDateTime + ".");
        List<Invoice> invoiceList = entityDao.getAll(Invoice.class);
        double amountSum = invoiceList.stream()
                .filter(invoice -> invoice.getDateAdded().isAfter(startOfPresentDay))
                .mapToDouble(invoice -> invoice.getAmount())
                .sum();

        System.err.println("Sum of amounts of present day invoices: " + amountSum);
    }
}
