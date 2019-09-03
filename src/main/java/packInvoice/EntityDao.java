package packInvoice;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EntityDao {
    public <T extends IBaseEntity> void saveOrUpdate(T entity) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try (Session session = factory.openSession()) {
            transaction = session.beginTransaction();

            session.saveOrUpdate(entity);

            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public <T extends IBaseEntity> List<T> getAll(Class<T> classT) {
        List<T> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            // Narzędzie do kreowania zapytania, do tworzenia query i budowania klauzuli 'whwrw'
            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Oiekt reprezentujący zapytanie o <typ generyczny>
            CriteriaQuery<T> criteriaQuery = cb.createQuery(classT);

            // reprezentuje tabelę 'Student' i tworzymy tą instancję żeby powiedzieć "do jakiej tabeli chcemy wykonać zapytanie"
            Root<T> rootTable = criteriaQuery.from(classT);

            // wykonanie select'a z tabeli
            criteriaQuery.select(rootTable);
//                    .where(cb.between(rootTable.get("nazwaKolumny"),
//                            LocalDateTime.now(),
//                            LocalDateTime.now().minusDays(5)));
            // znajdź rekordy których wartość kolumny jest pomiedzy dzisiaj, a 5 dni temu

            // wywołujemy zapytanie, wyniki zbieramy do listy
            list.addAll(session.createQuery(criteriaQuery).list());

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }



    public <T extends IBaseEntity> Optional<T> getById(Class<T> classT, Long id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {

            T entity = session.get(classT, id);
            return Optional.ofNullable(entity);
        }
    }

    public <T extends IBaseEntity> List<T> printSumInvoicesOfPresentDay(Class<T> classT) {
        List<T> list = new ArrayList<>();
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try (Session session = factory.openSession()) {
            // Narzędzie do kreowania zapytania, do tworzenia query i budowania klauzuli 'whwrw'
            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Oiekt reprezentujący zapytanie o <typ generyczny>
            CriteriaQuery<T> criteriaQuery = cb.createQuery(classT);

            // reprezentuje tabelę 'Student' i tworzymy tą instancję żeby powiedzieć "do jakiej tabeli chcemy wykonać zapytanie"
            Root<T> rootTable = criteriaQuery.from(classT);

            // wykonanie select'a z tabeli
            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDate localDate = localDateTime.toLocalDate();
            LocalDateTime startOfPresentDay = localDate.atStartOfDay();
            criteriaQuery.select(rootTable);
//                    .where(cb.sum(rootTable.get()))
//                    .where(cb.between(rootTable.get("nazwaKolumny"),
//                            LocalDateTime.now(),
//                            LocalDateTime.now().minusDays(5)));
            // znajdź rekordy których wartość kolumny jest pomiedzy dzisiaj, a 5 dni temu
            // znajdź sumę invoices of present day

            // wywołujemy zapytanie, wyniki zbieramy do listy
            list.addAll(session.createQuery(criteriaQuery).list());

        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return list;
    }
}
