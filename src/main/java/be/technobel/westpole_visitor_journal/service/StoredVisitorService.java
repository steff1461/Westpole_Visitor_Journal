package be.technobel.westpole_visitor_journal.service;

import be.technobel.westpole_visitor_journal.model.dto.StoredVisitorDto;
import be.technobel.westpole_visitor_journal.model.entity.StoredVisitorEntity;
import be.technobel.westpole_visitor_journal.repository.StoredVisitorRepo;
import be.technobel.westpole_visitor_journal.utils.mapper.StoredVisitorMapper;
import io.vertx.core.json.JsonObject;

import java.util.Optional;

public class StoredVisitorService {

    private final StoredVisitorRepo repository = new StoredVisitorRepo();

    public StoredVisitorService() {

    }

    public Optional<StoredVisitorDto> findIfStored(JsonObject bodyAsJson) {

        Optional<StoredVisitorEntity> svEntity = repository.findIfExist(bodyAsJson);

        return svEntity.map(StoredVisitorMapper::toDto);
    }

    public StoredVisitorEntity createNewStoredV(JsonObject object) {

        StoredVisitorDto dTo = StoredVisitorMapper.mapFromJson(object);
        StoredVisitorEntity entity = StoredVisitorMapper.toEntity(dTo);
        repository.create(entity);

        return entity;
    }
}
