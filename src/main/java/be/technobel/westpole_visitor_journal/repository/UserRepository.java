package be.technobel.westpole_visitor_journal.repository;

import be.technobel.westpole_visitor_journal.model.dto.UserDto;
import be.technobel.westpole_visitor_journal.model.entity.UserEntity;
import be.technobel.westpole_visitor_journal.utils.UtilsHiber;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class UserRepository extends BaseRepo<UserEntity> {

    public UserRepository(Class<UserEntity> type) {
        super(type);
    }

    public Optional<UserEntity> findUserByName(UserDto dto) {

        String userName = dto.getUserName();

        EntityManager manager = UtilsHiber.entityManagerInstance();
        TypedQuery<UserEntity> query = manager.createQuery("SELECT u from UserEntity u WHERE u.userName = :userName "
                , UserEntity.class);

        query.setParameter("userName", userName);

        UserEntity entity = query.getSingleResult();

        return Optional.of(entity);
    }
}
