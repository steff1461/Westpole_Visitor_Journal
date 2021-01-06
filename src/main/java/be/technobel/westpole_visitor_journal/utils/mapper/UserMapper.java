package be.technobel.westpole_visitor_journal.utils.mapper;

import be.technobel.westpole_visitor_journal.model.dto.UserDto;
import be.technobel.westpole_visitor_journal.model.entity.UserEntity;
import io.vertx.core.json.JsonObject;

public class UserMapper {

    private UserMapper() {

    }

    public static UserDto mapFromJson(JsonObject request) {

        UserDto dto = new UserDto();
        dto.setPassword(request.getString("password"))
                .setUserName(request.getString("userName"));

        return dto;
    }

    public static UserDto toDto(UserEntity entity) {

        UserDto dto = new UserDto();
        dto.setUserName(entity.getUserName())
                .setPassword(entity.getPassword());

        return dto;

    }

    public static UserEntity toEntity(UserDto dto) {

        UserEntity entity = new UserEntity();

        entity.setPassword(dto.getPassword())
                .setUserName(dto.getUserName());

        return entity;

    }

}
