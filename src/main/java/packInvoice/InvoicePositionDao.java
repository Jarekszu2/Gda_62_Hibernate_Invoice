package packInvoice;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class InvoicePositionDao {
    EntityDao entityDao = new EntityDao();

    public void printAllInvoicePositions() {
        List<InvoicePosition> invoicePositionList = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<InvoicePosition> criteriaQuery = criteriaBuilder.createQuery(InvoicePosition.class);
            Root<InvoicePosition> rootTable = criteriaQuery.from(InvoicePosition.class);
            criteriaQuery.select(rootTable);
            invoicePositionList.addAll(session.createQuery(criteriaQuery).list());
            invoicePositionList.forEach(System.err::println);
        } catch (HibernateException he) {
            he.printStackTrace();
        }
    }

    public void printAllInvoicePositions_mainD() {
        System.out.println();
        List<InvoicePosition> invoicePositionList = entityDao.getAll(InvoicePosition.class);
        invoicePositionList.forEach(System.err::println);
    }

    public void printAllInvoicePositionForInvoiceId(Long invoiceId) {
        List<InvoicePosition> invoicePositionList = new ArrayList<>();
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<InvoicePosition> criteriaQuery = criteriaBuilder.createQuery(InvoicePosition.class);
            Root<InvoicePosition> rootTable = criteriaQuery.from(InvoicePosition.class);
            criteriaQuery.select(rootTable)
                    .where(criteriaBuilder.equal(rootTable.get("invoice"), invoiceId));
            invoicePositionList.addAll(session.createQuery(criteriaQuery).list());
            invoicePositionList.forEach(System.err::println);
        }
    }

    public List<String> getPositionsWhereInvoiceId(Long invoiceId) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<String> criteriaQuery = criteriaBuilder.createQuery(String.class);
            Root<InvoicePosition> rootTable = criteriaQuery.from(InvoicePosition.class);
            criteriaQuery.select(rootTable.get("positionName"))
                    .where(criteriaBuilder.equal(rootTable.get("invoice"), invoiceId));
            return session.createQuery(criteriaQuery).getResultList();
        }
    }
}
