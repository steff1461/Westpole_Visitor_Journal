package be.technobel.westpole_visitor_journal.service;

import be.technobel.westpole_visitor_journal.model.dto.UserDto;
import be.technobel.westpole_visitor_journal.model.entity.UserEntity;
import be.technobel.westpole_visitor_journal.repository.UserRepository;
import be.technobel.westpole_visitor_journal.utils.mapper.UserMapper;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class UserService {

    private final UserRepository repository = new UserRepository(UserEntity.class);

    public UserService() {

    }

    public UserDto authenticateUser(UserDto oldDto) {

        Optional<UserEntity> userEntity = repository.findUserByName(oldDto);

        return userEntity.map(
                entity -> {
                    if (checkPassword(oldDto, entity))
                        return UserMapper.toDto(entity);
                    else return null;
                })
                .orElse(null);
    }

    public boolean checkCredentials(UserDto dto) {

        Optional<UserEntity> userEntity = repository.findUserByName(dto);

        return userEntity.map(entity -> entity.getPassword().equals(dto.getPassword()))
                .orElse(false);
    }

    public UserDto createUser(UserDto dto) {

        repository.create(UserMapper.toEntity(dto));

        return dto;
    }

    private boolean checkPassword(UserDto dto, UserEntity entity) {

        return BCrypt.checkpw(dto.getPassword(), entity.getPassword());
    }
}
