package be.technobel.westpole_visitor_journal.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UtilsHiber {

    private static EntityManagerFactory entityFactory;

    private UtilsHiber() {


    }

    public static EntityManager entityManagerInstance() {

        if (entityFactory == null) entityFactory = Persistence.createEntityManagerFactory("be.technobel" +
                                                                             ".westpole_visitor_journal");
        return entityFactory.createEntityManager();
    }

}
