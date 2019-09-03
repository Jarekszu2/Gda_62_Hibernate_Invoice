package packInvoice;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDao {
    EntityDao entityDao = new EntityDao();

    public void printAllInvoices() {
        List<Invoice> invoiceList = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            // Narzędzie do kreowania zapytania, do tworzenia query i budowania klauzuli 'where'
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            // Obiekt reprezentuje zapytanie o typ generyczny
            CriteriaQuery<Invoice> criteriaQuery = criteriaBuilder.createQuery(Invoice.class);

            // reprezentuje tabelę 'Invoice' i tworzymy tą instancję, żeby powiedzieć
            // 'do jakiej tabeli chcemy wykonać zapytanie'
            Root<Invoice> rootTable = criteriaQuery.from(Invoice.class);

            // wykonanie select'a z tabeli
            criteriaQuery.select(rootTable);

            // wywołujemy zapytanie, wyniki zbieramy do listy
            invoiceList.addAll(session.createQuery(criteriaQuery).list());

            invoiceList.forEach(System.err::println);
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }

    public void printAllInvoicesVer2_mainC() {
        System.out.println();
        List<Invoice> invoiceList = entityDao.getAll(Invoice.class);
        invoiceList.forEach(System.err::println);
    }

    public List<Invoice> getAllUnpaid() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoice> query = cb.createQuery(Invoice.class);
            Root<Invoice> root = query.from(Invoice.class);
            query.select(root).where(
                    cb.equal(root.get("paid"), 0));
            return session.createQuery(query).getResultList();
        }
    }

    public List<Invoice> getAllFromLastWeek() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Invoice> query = cb.createQuery(Invoice.class);
            Root<Invoice> root = query.from(Invoice.class);
            query.select(root).where(
                    cb.between(
                            root.get("dateAdded"),
                            LocalDateTime.now().minusDays(7),
                            LocalDateTime.now()
                    )
            ).orderBy(cb.asc(root.get("dateAdded")));

            return session.createQuery(query).setMaxResults(1).getResultList(); // albo getSingleResult na końcu (listujemy jeden, najstarszy wynik)
        }
    }

    public Double getAmountFromInvoiceId(Long invoiceId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Invoice> root = query.from(Invoice.class);

            query.select(root.get("amount")).where(
                    cb.equal(
                            root.get("id"),
                            invoiceId
                    )
            );
            Double amount = session.createQuery(query).getSingleResult();
            return amount;
        }
    }

    public void getSumInvoicesOfPresentDay() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Double> query = cb.createQuery(Double.class);
            Root<Invoice> root = query.from(Invoice.class);

            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDateTime startOfPresentDay = localDate.atStartOfDay();

            query.select(cb.sum(root.get("amount"))).where(
                    cb.between(
                            root.get("dateAdded"),
                            startOfPresentDay,
                            LocalDateTime.now()
                    )
            );
            Double sum = session.createQuery(query).getSingleResult();
            System.err.println("Range of time: from " + startOfPresentDay + " to " + localDateTime + ".");
            System.err.println(sum);
        }
    }
}
