package repository.orm;

import model.Office;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import repository.IOfficeRepository;
import repository.RepositoryException;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OfficeRepositoryOrm implements IOfficeRepository {

    private static final Logger logger = LogManager.getLogger();
    private static org.hibernate.SessionFactory sessionFactory;
    public OfficeRepositoryOrm() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    @Override
    public void add(Office item) throws RepositoryException {
        //Nu adaugam office
    }

    @Override
    public void update(Office item) throws RepositoryException {
        //Nu fac update la office
    }

    @Override
    public Optional<Office> find(Long itemID) throws RepositoryException {
        logger.traceEntry("Param {}", itemID);

        ormModel.Office officeORM=null;
        Connection connection;
        try (var session= sessionFactory.openSession()){
            Transaction tx=session.beginTransaction();
            officeORM= session.createQuery("select entity from Office entity where id=:id_itemId", ormModel.Office.class).setParameter("id_itemId",itemID).getSingleResult();

            tx.commit();
        } catch (Exception e) {
            throw logger.throwing(new RepositoryException(e));
        }

        return Optional.of(new Office(officeORM.getId(), officeORM.getUsername(), officeORM.getPassword()));
    }

    @Override
    public Optional<Office> findByUsername(String username) throws RepositoryException {
        logger.traceEntry("Param {}", username);
        ormModel.Office officeORM=null;
        Connection connection;
        try (var session= sessionFactory.openSession()){
            Transaction tx=session.beginTransaction();
            officeORM= session.createQuery("select entity from Office entity where username=:username", ormModel.Office.class).setParameter("username",username).getSingleResult();

            tx.commit();
        } catch (Exception e) {
            throw logger.throwing(new RepositoryException(e));
        }
        System.out.println(officeORM.getUsername());
        return Optional.of(new Office(officeORM.getId(), officeORM.getUsername(), officeORM.getPassword()));
    }

    @Override
    public List<Office> getAll() throws RepositoryException {
        logger.traceEntry("Param none");
        List<Office> list = new ArrayList<>();

        ormModel.Office officeORM=null;
        try (var session= sessionFactory.openSession()){
            Transaction tx=session.beginTransaction();
            list= session.createQuery("select entity from Office entity", ormModel.Office.class)
                    .   getResultList()
                    .stream().map(e->new Office(e.getId(),e.getUsername(),e.getPassword())).toList();
            tx.commit();
        } catch (Exception e) {
            throw logger.throwing(new RepositoryException(e));
        }

        return list;
    }

    @Override
    public void delete(Long itemID) throws RepositoryException {

    }
}
