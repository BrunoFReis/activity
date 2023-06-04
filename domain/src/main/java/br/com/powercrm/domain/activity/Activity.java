package br.com.powercrm.domain.activity;

import br.com.powercrm.domain.AggregateRoot;
import br.com.powercrm.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Activity extends AggregateRoot<ActivityID> implements Cloneable {

    private Long creativeCompanyUserId;
    private Long responsibleCompanyUserId;
    private Long companyId;
    private Long negotiationId;
    private String description;
    private String response;
    private ActivityType type;
    private ActivityStatus status;
    private Instant scheduled;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant finishedAt;

    private Activity(
            final ActivityID anId,
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final Instant aScheduleDate,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aFinisheDate
    ) {
        super(anId);
        this.creativeCompanyUserId = aCreativeCompanyUserId;
        this.responsibleCompanyUserId = aResponsibleCompanyUserId;
        this.companyId = aCompanyId;
        this.negotiationId = aNegotiationId;
        this.description = aDescription;
        this.response = aResponse;
        this.type = aType;
        this.status = ActivityStatus.ACTIVE;
        this.scheduled = aScheduleDate;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.finishedAt = aFinisheDate;
    }

    private Activity(
            final ActivityID anId,
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final ActivityStatus aStatus,
            final Instant aScheduleDate,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aFinisheDate
    ) {
        super(anId);
        this.creativeCompanyUserId = aCreativeCompanyUserId;
        this.responsibleCompanyUserId = aResponsibleCompanyUserId;
        this.companyId = aCompanyId;
        this.negotiationId = aNegotiationId;
        this.description = aDescription;
        this.response = aResponse;
        this.type = aType;
        this.status = aStatus;
        this.scheduled = Objects.requireNonNull(aScheduleDate, "'scheduled' should not be null");
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        this.finishedAt = aFinisheDate;
    }

    public static Activity newActivity(
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final Instant aScheduleDate
    ) {
        final var id = ActivityID.unique();
        final var now = Instant.now();

        return new Activity(
                id,
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aScheduleDate,
                now,
                now,
                null
        );
    }

    public static Activity newActivityFromId(
            final String anId,
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final Instant aScheduleDate
    ) {
        final var id = ActivityID.from(anId);
        final var now = Instant.now();

        return new Activity(
                id,
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aScheduleDate,
                now,
                now,
                null
        );
    }

    public static Activity with(
            final ActivityID anId,
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final ActivityStatus aStatus,
            final Instant aScheduled,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aFinishedAt) {
        return new Activity(
                anId,
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aStatus,
                aScheduled,
                aCreatedAt,
                anUpdatedAt,
                aFinishedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new ActivityValidator(this, handler).validate();
    }


    public Activity canceled() {
        this.status = ActivityStatus.CANCELED;
        this.updatedAt = Instant.now();

        return this;
    }

    public Activity activated() {
        this.status = ActivityStatus.ACTIVE;
        this.updatedAt = Instant.now();

        return this;
    }

    public Activity completed() {
        if (getFinishedAt() == null) {
            this.finishedAt = Instant.now();
        }

        this.status = ActivityStatus.COMPLETED;
        this.updatedAt = Instant.now();

        return this;
    }

    public Activity update(
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final ActivityStatus aStatus,
            final Instant aScheduleDate
    ) {
        if (aStatus.equals(ActivityStatus.ACTIVE)) {
            activated();
        }

        if (aStatus.equals(ActivityStatus.CANCELED)) {
            canceled();
        }

        if (aStatus.equals(ActivityStatus.COMPLETED)) {
            completed();
        }

        this.creativeCompanyUserId = aCreativeCompanyUserId;
        this.responsibleCompanyUserId = aResponsibleCompanyUserId;
        this.companyId = aCompanyId;
        this.negotiationId = aNegotiationId;
        this.description = aDescription;
        this.response = aResponse;
        this.type = aType;
        this.scheduled = aScheduleDate;
        this.updatedAt = Instant.now();

        return this;
    }

    public ActivityID getId() {
        return id;
    }

    public Long getCreativeCompanyUserId() {
        return creativeCompanyUserId;
    }

    public Long getResponsibleCompanyUserId() {
        return responsibleCompanyUserId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getNegotiationId() {
        return negotiationId;
    }

    public String getDescription() {
        return description;
    }

    public String getResponse() {
        return response;
    }

    public ActivityType getType() {
        return type;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public Instant getScheduled() {
        return scheduled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    @Override
    public Activity clone() {
        try {
            return (Activity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}