package be.technobel.westpole_visitor_journal.repository;

import be.technobel.westpole_visitor_journal.repository.entity.VisitorEntity;
import be.technobel.westpole_visitor_journal.utils.UtilsHiber;
import io.vertx.core.json.JsonObject;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class VisitorRepo extends BaseRepo<VisitorEntity> {

    public VisitorRepo() {

        super(VisitorEntity.class);
    }

    public boolean signOutByName(JsonObject object) {

        String lName = object.getString("lName");
        LocalDate curDate = LocalDate.now();

        EntityManager manager = UtilsHiber.entityManagerInstance();
        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v INNER JOIN v.svEntity sv" +
                                                                      " WHERE  " +
                                                                      " sv.lName = :lName AND v.curDate = :curDate " +
                                                                      "AND v.outTime IS NULL",
                                                              VisitorEntity.class);
        query.setParameter("lName", lName);
        query.setParameter("curDate", curDate);

        List<VisitorEntity> visitors = query.getResultList();

        if (visitors != null) {

            if (visitors.size() == 1) {

                visitors.get(0).setOutTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS));
                this.update(visitors.get(0));
                return true;
            }
        }

        return false;
    }

    public boolean signOutVisitor(JsonObject object) {

        String fName = object.getString("fName");
        String lName = object.getString("lName");
        LocalDate curDate = LocalDate.now();

        EntityManager manager = UtilsHiber.entityManagerInstance();
        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v INNER JOIN v.svEntity " +
                                                                      "sv  WHERE " +
                                                                      "sv.fName = :fName AND sv.lName = :lName " +
                                                                      "AND v.curDate = :curDate AND v.outTime" +
                                                                      " IS NULL",
                                                              VisitorEntity.class);

        query.setParameter("fName", fName);
        query.setParameter("lName", lName);
        query.setParameter("curDate", curDate);
        List<VisitorEntity> visitors = query.getResultList();

        if (visitors != null) {

            if (visitors.size() > 0) {

                visitors.stream()
                        .map(v -> v.setOutTime(LocalTime.now().truncatedTo(ChronoUnit.SECONDS)))
                        .forEach(this::update);
                return true;
            }
        }
        return false;
    }

    public List<VisitorEntity> findAllByDay(LocalDate date) {


        EntityManager manager = UtilsHiber.entityManagerInstance();
        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE v.curDate = :date " +
                                                                      "ORDER BY v.curDate DESC",
                                                              VisitorEntity.class);

        query.setParameter("date", date.toString());

        return query.getResultList();
    }

    public List<VisitorEntity> findAllByMonth(LocalDate date) {

        EntityManager manager = UtilsHiber.entityManagerInstance();


        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE MONTH(v.curDate" +
                                                                      ")" +
                                                                      "  = MONTH(:date) ORDER BY v.curDate DESC",
                                                              VisitorEntity.class);

        query.setParameter("date", date);

        return query.getResultList();
    }

    public List<VisitorEntity> findAllByWeek(LocalDate startWeek) {

        EntityManager manager = UtilsHiber.entityManagerInstance();

        LocalDate endWeek = startWeek.plusDays(6);

        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT v FROM VisitorEntity v WHERE v.curDate >= " +
                                                                      ":startDate AND v.curDate <= :endDate ORDER BY " +
                                                                      "v.curDate DESC",
                                                              VisitorEntity.class);

        query.setParameter("startDate", startWeek);
        query.setParameter("endDate", endWeek);

        return query.getResultList();
    }

    public List<VisitorEntity> findAllByCompany(String companyName) {

        EntityManager manager = UtilsHiber.entityManagerInstance();

        TypedQuery<VisitorEntity> query = manager.createQuery("SELECT  v from VisitorEntity v INNER JOIN v.svEntity " +
                                                                      "sv WHERE sv.companyName = " +
                                                                      ":companyName ORDER BY v.curDate DESC",
                                                              VisitorEntity.class);
        query.setParameter("companyName", companyName);

        return query.getResultList();
    }
}




