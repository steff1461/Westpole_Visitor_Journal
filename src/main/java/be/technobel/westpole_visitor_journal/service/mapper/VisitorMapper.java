package be.technobel.westpole_visitor_journal.service.mapper;

import be.technobel.westpole_visitor_journal.repository.VisitorEntity;
import be.technobel.westpole_visitor_journal.service.model.VisitorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;

import java.time.LocalDate;
import java.time.LocalTime;

public class VisitorMapper {

    public static VisitorDto toDto(VisitorEntity entity) {

        VisitorDto dTo = new VisitorDto();
        dTo.setId(entity.getId())
                .setfName(entity.getfName())
                .setlName(entity.getlName())
                .setlPlate(entity.getlPLate())
                .setCompanyName(entity.getCompanyName())
                .setContactName(entity.getContactName())
                .setCurDate(entity.getCurDate())
                .setInTime(entity.getInTime())
                .setOutTime(entity.getOutTime());
        return dTo;
    }

    public static VisitorEntity toEntity(VisitorDto model) {

        VisitorEntity entity = new VisitorEntity();

        entity.setfName(model.getfName())
                .setlName(model.getlName())
                .setlPLate(model.getlPlate())
                .setCompanyName(model.getCompanyName())
                .setContactName(model.getContactName())
                .setCurDate(LocalDate.parse(model.getCurDate()))
                .setInTime(LocalTime.parse(model.getInTime()));

        return entity;
    }

    public static String mapAsJson(VisitorDto dto) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        return null;
    }

    public static VisitorDto mapFromJson(JsonObject request) {

        VisitorDto dTo = new VisitorDto();

        String fname = request.getString("fName");
        String lName = request.getString("lName");
        String lPlate = request.getString("lPlate");
        String companyName = request.getString("companyName");
        String contactName = request.getString("contactName");
        LocalDate curDate = LocalDate.now();
        LocalTime inTime = LocalTime.now();

        dTo.setfName(fname)
                .setlName(lName)
                .setlPlate(lPlate)
                .setCompanyName(companyName)
                .setContactName(contactName)
                .setCurDate(curDate)
                .setInTime(inTime);

        return dTo;
    }
}
