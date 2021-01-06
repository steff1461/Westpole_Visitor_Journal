package be.technobel.westpole_visitor_journal.service;

import be.technobel.westpole_visitor_journal.model.dto.VisitorDto;
import be.technobel.westpole_visitor_journal.model.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.repository.VisitorRepo;
import be.technobel.westpole_visitor_journal.utils.mapper.StoredVisitorMapper;
import be.technobel.westpole_visitor_journal.utils.mapper.VisitorMapper;
import io.vertx.core.json.JsonObject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class VisitorService {

    private final VisitorRepo repository = new VisitorRepo();

    public VisitorService() {

    }

    //POST

    public Optional<String> signOutByName(JsonObject object) {

        if (repository.signOutByName(object)) return Optional.of(object.getString("lName"));
        else return Optional.empty();
    }

    public Optional<String> signOutVisitor(JsonObject object) {

        if (repository.signOutVisitor(object)) return Optional.of(object.getString("fName"));
        else return Optional.empty();
    }


    public Optional<String> createNewVisitor(StoredVisitorEntity svEntity, String contactName) {

        VisitorDto dTo = VisitorMapper.mapFromStoredV(StoredVisitorMapper.toDto(svEntity));
        dTo.setContactName(contactName);
        repository.merge(VisitorMapper.toEntity(dTo, svEntity));
        return Optional.of(dTo.getfName());
    }

    //GET

    public Optional<List<VisitorDto>> findAll() {

        List<VisitorDto> visitors =
                repository
                        .findAll()
                        .stream()
                        .map(VisitorMapper::toDto)
                        .collect(Collectors.toList());

        return Optional.of(visitors);
    }

    public Optional<List<VisitorDto>> findAllByMonth(LocalDate date) {

        List<VisitorDto> visitors =
                repository
                        .findAllByMonth(date).stream()
                        .map(VisitorMapper::toDto)
                        .collect(Collectors.toList());

        return Optional.of(visitors);
    }

    public Optional<List<VisitorDto>> findAllByWeek(LocalDate date) {

        List<VisitorDto> visitors =
                repository
                        .findAllByWeek(findStartingWeek(date)).stream()
                        .map(VisitorMapper::toDto)
                        .collect(Collectors.toList());

        return Optional.of(visitors);
    }

    public Optional<List<VisitorDto>> findAllByDay(LocalDate date) {

        List<VisitorDto> visitors =
                repository
                        .findAllByDay(date)
                        .stream()
                        .map(VisitorMapper::toDto)
                        .collect(Collectors.toList());

        return Optional.of(visitors);
    }

    public Optional<List<VisitorDto>> findAllCurrent() {

        List<VisitorDto> visitors =
                repository
                        .findAllByDay(LocalDate.now())
                        .stream()
                        .filter(visitorEntity -> visitorEntity.getOutTime()==null)
                        .map(VisitorMapper::toDto)
                        .collect(Collectors.toList());

        return Optional.of(visitors);
    }


    public Optional<List<VisitorDto>> findAllByCompany(String companyName) {
        List<VisitorDto> visitors =
                repository
                        .findAllByCompany(companyName).stream()
                        .map(VisitorMapper::toDto)
                        .collect(Collectors.toList());

        return Optional.of(visitors);
    }

    private LocalDate findStartingWeek(LocalDate date) {

        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = date.get(woy);

        return LocalDate.of(2020, 6, 10)
                .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekNumber)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }
}
