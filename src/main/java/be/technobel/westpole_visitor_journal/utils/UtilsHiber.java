package be.technobel.westpole_visitor_journal.utils;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class UtilsHiber {

    private static EntityManager manager;

    private UtilsHiber() {

    }

    public static EntityManager entityManagerInstance() {

        if (manager == null)
            manager = Persistence
                    .createEntityManagerFactory("be.technobel.westpole_visitor_journal")
                    .createEntityManager();
        return manager;
    }

}
