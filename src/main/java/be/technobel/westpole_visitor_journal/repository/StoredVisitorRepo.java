package be.technobel.westpole_visitor_journal.repository;

import be.technobel.westpole_visitor_journal.repository.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.utils.UtilsHiber;
import io.vertx.core.json.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class StoredVisitorRepo extends BaseRepo<StoredVisitorEntity> {

    public StoredVisitorRepo() {

        super(StoredVisitorEntity.class);
    }

    public Optional<StoredVisitorEntity> findIfExist(JsonObject object) {

        String fName = object.getString("fName");
        String lName = object.getString("lName");

        EntityManager manager = UtilsHiber.entityManagerInstance();

        TypedQuery<StoredVisitorEntity> query = manager.createQuery("SELECT sv FROM StoredVisitorEntity sv WHERE sv" +
                                                                            ".fName = " +
                                                                            ":fName " +
                                                                            "and sv" +
                                                                            ".lName = :lName",
                                                                    StoredVisitorEntity.class);
        query.setParameter("fName", fName);
        query.setParameter("lName", lName);


        try {

            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {

            return Optional.empty();
        }
    }
}
