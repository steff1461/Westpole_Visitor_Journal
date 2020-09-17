package be.technobel.westpole_visitor_journal.utils.mapper;

import be.technobel.westpole_visitor_journal.repository.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.service.model.StoredVisitorDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;

public class StoredVisitorMapper {

    private StoredVisitorMapper() {

    }

    public static StoredVisitorDto toDto(StoredVisitorEntity entity) {

        StoredVisitorDto dTo = new StoredVisitorDto();

        dTo.setId(entity.getId())
                .setfName(entity.getfName())
                .setlName(entity.getlName())
                .setCompanyName(entity.getCompanyName())
                .setlPlate(entity.getlPLate());

        return dTo;
    }

    public static StoredVisitorEntity toEntity(StoredVisitorDto dTo) {

        StoredVisitorEntity entity = new StoredVisitorEntity();

        entity.setId(dTo.getId())
                .setfName(dTo.getfName())
                .setlName(dTo.getlName())
                .setCompanyName(dTo.getCompanyName())
                .setlPLate(dTo.getlPlate());

        return entity;
    }

    public static StoredVisitorDto mapFromJson(JsonObject object) {

        StoredVisitorDto dTo = new StoredVisitorDto();
        dTo.setfName(object.getString("fName"))
                .setlName(object.getString("lName"))
                .setlPlate(object.getString("lPlate"))
                .setCompanyName(object.getString("companyName"));

        return dTo;
    }

    public static String mapAsJson(StoredVisitorDto dto) {

        ObjectMapper mapper = new ObjectMapper();

        try {

            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
        return null;
    }


}
