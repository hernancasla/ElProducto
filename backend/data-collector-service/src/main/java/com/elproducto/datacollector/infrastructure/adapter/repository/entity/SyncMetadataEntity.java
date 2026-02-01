package com.elproducto.datacollector.infrastructure.adapter.repository.entity;

import com.elproducto.datacollector.domain.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entidad R2DBC para la tabla sync_metadata
 */
@Table("sync_metadata")
public class SyncMetadataEntity implements Persistable<Long> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Id
    private Long id;

    @Transient
    private boolean isNew = true;

    @Column("entity_type")
    private String entityType;

    @Column("sync_scope")
    private String syncScope;

    @Column("sync_status")
    private String syncStatus;

    @Column("sync_strategy")
    private String syncStrategy;

    @Column("started_at")
    private LocalDateTime startedAt;

    @Column("completed_at")
    private LocalDateTime completedAt;

    @Column("records_processed")
    private Integer recordsProcessed;

    @Column("records_created")
    private Integer recordsCreated;

    @Column("records_updated")
    private Integer recordsUpdated;

    @Column("records_deleted")
    private Integer recordsDeleted;

    @Column("records_skipped")
    private Integer recordsSkipped;

    @Column("api_calls_made")
    private Integer apiCallsMade;

    @Column("api_errors")
    private Integer apiErrors;

    @Column("error_message")
    private String errorMessage;

    @Column("error_details")
    private String errorDetails;  // JSON string

    @Column("retry_count")
    private Integer retryCount;

    @Column("triggered_by")
    private String triggeredBy;

    @Column("trigger_event")
    private String triggerEvent;

    @Column("parameters")
    private String parameters;  // JSON string

    @Column("created_at")
    private LocalDateTime createdAt;

    public SyncMetadataEntity() {
    }

    public static SyncMetadataEntity fromDomain(SyncMetadata metadata) {
        SyncMetadataEntity entity = new SyncMetadataEntity();
        entity.id = metadata.id();
        entity.entityType = metadata.entityType().name();
        entity.syncScope = metadata.syncScope();
        entity.syncStatus = metadata.status().name();
        entity.syncStrategy = metadata.strategy().name();
        entity.startedAt = metadata.startedAt();
        entity.completedAt = metadata.completedAt();
        entity.recordsProcessed = metadata.recordsProcessed();
        entity.recordsCreated = metadata.recordsCreated();
        entity.recordsUpdated = metadata.recordsUpdated();
        entity.recordsDeleted = metadata.recordsDeleted();
        entity.recordsSkipped = metadata.recordsSkipped();
        entity.apiCallsMade = metadata.apiCallsMade();
        entity.apiErrors = metadata.apiErrors();
        entity.errorMessage = metadata.errorMessage();
        entity.errorDetails = toJson(metadata.errorDetails());
        entity.retryCount = metadata.retryCount();
        entity.triggeredBy = metadata.triggeredBy().name();
        entity.triggerEvent = metadata.triggerEvent();
        entity.parameters = toJson(metadata.parameters());
        entity.createdAt = metadata.createdAt() != null ? metadata.createdAt() : LocalDateTime.now();
        entity.isNew = metadata.id() == null;
        return entity;
    }

    public SyncMetadata toDomain() {
        return new SyncMetadata(
            id,
            EntityType.valueOf(entityType),
            syncScope,
            SyncStatus.valueOf(syncStatus),
            SyncStrategy.valueOf(syncStrategy),
            startedAt,
            completedAt,
            recordsProcessed != null ? recordsProcessed : 0,
            recordsCreated != null ? recordsCreated : 0,
            recordsUpdated != null ? recordsUpdated : 0,
            recordsDeleted != null ? recordsDeleted : 0,
            recordsSkipped != null ? recordsSkipped : 0,
            apiCallsMade != null ? apiCallsMade : 0,
            apiErrors != null ? apiErrors : 0,
            errorMessage,
            fromJson(errorDetails),
            retryCount != null ? retryCount : 0,
            SyncTrigger.valueOf(triggeredBy),
            triggerEvent,
            fromJson(parameters),
            createdAt
        );
    }

    private static String toJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static Map<String, Object> fromJson(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public void markAsNotNew() {
        this.isNew = false;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew || id == null;
    }

    // Getters y Setters
    public void setId(Long id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSyncScope() {
        return syncScope;
    }

    public void setSyncScope(String syncScope) {
        this.syncScope = syncScope;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncStrategy() {
        return syncStrategy;
    }

    public void setSyncStrategy(String syncStrategy) {
        this.syncStrategy = syncStrategy;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(Integer recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }

    public Integer getRecordsCreated() {
        return recordsCreated;
    }

    public void setRecordsCreated(Integer recordsCreated) {
        this.recordsCreated = recordsCreated;
    }

    public Integer getRecordsUpdated() {
        return recordsUpdated;
    }

    public void setRecordsUpdated(Integer recordsUpdated) {
        this.recordsUpdated = recordsUpdated;
    }

    public Integer getRecordsDeleted() {
        return recordsDeleted;
    }

    public void setRecordsDeleted(Integer recordsDeleted) {
        this.recordsDeleted = recordsDeleted;
    }

    public Integer getRecordsSkipped() {
        return recordsSkipped;
    }

    public void setRecordsSkipped(Integer recordsSkipped) {
        this.recordsSkipped = recordsSkipped;
    }

    public Integer getApiCallsMade() {
        return apiCallsMade;
    }

    public void setApiCallsMade(Integer apiCallsMade) {
        this.apiCallsMade = apiCallsMade;
    }

    public Integer getApiErrors() {
        return apiErrors;
    }

    public void setApiErrors(Integer apiErrors) {
        this.apiErrors = apiErrors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getTriggerEvent() {
        return triggerEvent;
    }

    public void setTriggerEvent(String triggerEvent) {
        this.triggerEvent = triggerEvent;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}