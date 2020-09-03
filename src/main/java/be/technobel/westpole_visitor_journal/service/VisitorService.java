package be.technobel.westpole_visitor_journal.service;

import be.technobel.westpole_visitor_journal.repository.VisitorRepo;
import be.technobel.westpole_visitor_journal.service.mapper.VisitorMapper;
import be.technobel.westpole_visitor_journal.service.model.VisitorDto;
import io.vertx.core.json.JsonObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.Locale;

public class VisitorService {

    private final VisitorRepo repository;

    public VisitorService() {

        repository = new VisitorRepo();
    }

    public String createNewVisitor(JsonObject object) {

        VisitorDto dTo = VisitorMapper.mapFromJson(object);
        repository.create(VisitorMapper.toEntity(dTo));
        return dTo.getfName();
    }

    public StringBuilder findAll() {

        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("[");

        repository.findAll()
                .stream()
                .map(VisitorMapper::toDto).sorted(Comparator.comparing(VisitorDto::getCurDate).reversed())
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
                .map(VisitorMapper::toDto).sorted(Comparator.comparing(VisitorDto::getCurDate))
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
                .map(VisitorMapper::toDto).sorted(Comparator.comparing(VisitorDto::getCurDate))
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
                .map(VisitorMapper::toDto).sorted(Comparator.comparing(VisitorDto::getInTime))
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

    private LocalDate findStartingWeek(LocalDate date) {

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = date.get(woy);

        return LocalDate.of(2020, 6, 10)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public String signOutVisitor(JsonObject object) {

        if (repository.signOutVisitor(object)) return object.getString("fName");
        else return "NONE";

    }
}
