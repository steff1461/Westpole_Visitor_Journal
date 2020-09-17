package be.technobel.westpole_visitor_journal.service;

import be.technobel.westpole_visitor_journal.repository.VisitorRepo;
import be.technobel.westpole_visitor_journal.repository.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.service.model.VisitorDto;
import be.technobel.westpole_visitor_journal.utils.mapper.StoredVisitorMapper;
import be.technobel.westpole_visitor_journal.utils.mapper.VisitorMapper;
import io.vertx.core.json.JsonObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class VisitorService {

    private final VisitorRepo repository = new VisitorRepo();

    public VisitorService() {


    }

    //POST

    public String signOutByName(JsonObject object){

        if (repository.signOutByName(object)) return object.getString("lName");
        else return "NONE";
    }

    public String signOutVisitor(JsonObject object) {

        if (repository.signOutVisitor(object)) return object.getString("fName");
        else return "NONE";

    }


    public String createNewVisitor( StoredVisitorEntity svEntity,String contactName) {

        VisitorDto dTo = VisitorMapper.mapFromStoredV(StoredVisitorMapper.toDto(svEntity));
        dTo.setContactName(contactName);
        repository.merge(VisitorMapper.toEntity(dTo,svEntity));
        return dTo.getfName();
    }

    //GET

    public StringBuilder findAll() {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        repository.findAll()
                .stream()
                .map(VisitorMapper::toDto)
                .map(VisitorMapper::mapAsJson)
                .forEach(s -> sBuilder.append(s).append(","));

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("]");
        return sBuilder;
    }

    public StringBuilder findAllByMonth(LocalDate date) {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        repository.findAllByMonth(date)
                .stream()
                .map(VisitorMapper::toDto)
                .map(VisitorMapper::mapAsJson)
                .forEach(s -> sBuilder.append(s).append(","));

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("]");

        return sBuilder;
    }

    public StringBuilder findAllByWeek(LocalDate date) {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        repository.findAllByWeek(findStartingWeek(date))
                .stream()
                .map(VisitorMapper::toDto)
                .map(VisitorMapper::mapAsJson)
                .forEach(s -> sBuilder.append(s).append(","));

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("]");

        return sBuilder;
    }

    public StringBuilder findAllByDay(LocalDate date) {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        repository.findAllByDay(date)
                .stream()
                .map(VisitorMapper::toDto)
                .map(VisitorMapper::mapAsJson)
                .forEach(s -> sBuilder.append(s).append(","));

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("]");

        return sBuilder;
    }

    public StringBuilder findAllCurrent() {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");
        repository.findAllByDay(LocalDate.now())
                .stream()
                .filter(entity -> entity.getOutTime() == null)
                .map(VisitorMapper::toDto)
                .map(VisitorMapper::mapAsJson)
                .forEach(s -> sBuilder.append(s).append(","));

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("]");
        return sBuilder;
    }


    public StringBuilder findAllByCompany(String companyName) {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        repository.findAllByCompany(companyName)
                .stream()
                .map(VisitorMapper::toDto)
                .map(VisitorMapper::mapAsJson)
                .forEach(s -> sBuilder.append(s).append(","));

        sBuilder.deleteCharAt(sBuilder.length() - 1);
        sBuilder.append("]");
        return sBuilder;
    }

    private LocalDate findStartingWeek(LocalDate date) {

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = date.get(woy);

        return LocalDate.of(2020, 6, 10)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }


}
