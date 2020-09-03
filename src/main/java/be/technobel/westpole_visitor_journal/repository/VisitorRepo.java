package be.technobel.westpole_visitor_journal.repository;

import be.technobel.westpole_visitor_journal.utils.UtilsHiber;
import io.vertx.core.json.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class VisitorRepo extends BaseRepo<VisitorEntity> {

    public VisitorRepo() {

        super(VisitorEntity.class);
    }


    public boolean signOutVisitor(JsonObject object) {

        String fName = object.getString("fName");
        String lName = object.getString("lName");

        EntityManager manager = UtilsHiber.entityManagerInstance();
        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE v.fName = :fName " +
                                                                      "AND v.lName = :lName",
                                                              VisitorEntity.class);
        query.setParameter("fName", fName);
        query.setParameter("lName", lName);

        VisitorEntity visitor = query.getSingleResult();

        if (visitor != null) {

            visitor.setOutTime(LocalTime.now());
            this.update(visitor);
            return true;
        } else return false;
    }

    public List<VisitorEntity> findAllByDay(LocalDate date) {


        EntityManager manager = UtilsHiber.entityManagerInstance();
        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE v.curDate = :date",
                                                              VisitorEntity.class);

        query.setParameter("date", date.toString());

        return query.getResultList();


//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<VisitorEntity> query = criteriaBuilder.createQuery(VisitorEntity.class);
//        Root<VisitorEntity> root = query.from(VisitorEntity.class);
//
//        manager.getTransaction().begin();
//
//        query.select(root)
//                .where(criteriaBuilder.equal(root.get("curDate"), date.toString()));
//
//        List<VisitorEntity> visitors = manager.createQuery(query).getResultList();
//
//        manager.getTransaction().commit();
//        manager.close();
//        return visitors;
    }

    public List<VisitorEntity> findAllByMonth(LocalDate date) {

        EntityManager manager = UtilsHiber.entityManagerInstance();


        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE MONTH(v.curDate" +
                                                                      ")" +
                                                                      "  = MONTH(:date)", VisitorEntity.class);

        query.setParameter("date", date);

        return query.getResultList();

//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<VisitorEntity> query = criteriaBuilder.createQuery(VisitorEntity.class);
//        Root<VisitorEntity> root = query.from(VisitorEntity.class);
//
//        query.select(root)
//                .where(criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("curDate")),
//                                             month));
//
//
//        return manager
//                .createQuery(query)
//                .getResultList();

    }

    public List<VisitorEntity> findAllByWeek(LocalDate startWeek) {

        EntityManager manager = UtilsHiber.entityManagerInstance();

        LocalDate endWeek = startWeek.plusDays(6);

        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE v.curDate >= " +
                                                                      ":startDate AND v.curDate <= :endDate",
                                                              VisitorEntity.class);

        query.setParameter("startDate", startWeek);
        query.setParameter("endDate", endWeek);

        return query.getResultList();

//        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
//        CriteriaQuery<VisitorEntity> query = criteriaBuilder.createQuery(VisitorEntity.class);
//        Root<VisitorEntity> root = query.from(VisitorEntity.class);
//
//        query.select(root)
//                .where(criteriaBuilder.between(root.get("curDate"), startWeek, endWeek));
//
//        return manager
//                .createQuery(query)
//                .getResultList();
    }

}




