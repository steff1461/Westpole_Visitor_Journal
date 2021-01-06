package be.technobel.westpole_visitor_journal.utils.mapper;

import be.technobel.westpole_visitor_journal.model.dto.StoredVisitorDto;
import be.technobel.westpole_visitor_journal.model.dto.VisitorDto;
import be.technobel.westpole_visitor_journal.model.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.model.entity.VisitorEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class VisitorMapper {

    private VisitorMapper() {

    }

    public static VisitorDto toDto(VisitorEntity entity) {

        VisitorDto dTo = new VisitorDto();
        dTo.setId(entity.getId())
                .setfName(entity.getSvEntity().getfName())
                .setlName(entity.getSvEntity().getlName())
                .setCompanyName(entity.getSvEntity().getCompanyName())
                .setContactName(entity.getContactName())
                .setlPlate(entity.getSvEntity().getlPLate())
                .setCurDate(entity.getCurDate())
                .setInTime(entity.getInTime())
                .setOutTime(entity.getOutTime());
        return dTo;
    }

    public static VisitorEntity toEntity(VisitorDto dTo, StoredVisitorEntity svEntity) {

        VisitorEntity entity = new VisitorEntity();

        entity.setContactName(dTo.getContactName())
                .setCurDate(LocalDate.parse(dTo.getCurDate()))
                .setInTime(LocalTime.parse(dTo.getInTime()))
                .setSvEntity(svEntity);

        return entity;
    }
    public static VisitorDto mapFromStoredV(StoredVisitorDto svDto) {

        VisitorDto dTo = new VisitorDto();

        dTo.setfName(svDto.getfName())
                .setlName(svDto.getlName())
                .setCompanyName(svDto.getCompanyName())
                .setlPlate(svDto.getlPlate())
                .setInTime( LocalTime.now().truncatedTo(ChronoUnit.SECONDS))
                .setCurDate(LocalDate.now())
                .setContactName("NONE");

        return dTo;
    }
}
