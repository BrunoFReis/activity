package br.com.powercrm.infrastructure.activity.persistence;

import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "activities")
public class ActivityJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creative_company_user_id", nullable = false)
    private Long creativeCompanyUserId;

    @Column(name = "responsible_company_user_id", nullable = false)
    private Long responsibleCompanyUserId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "negotiation_id", nullable = false)
    private Long negotiationId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "response")
    private String response;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "scheduled", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant scheduled;

    @Column(name = "createdAt", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updatedAt", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "finishedAt", columnDefinition = "DATETIME(6)")
    private Instant finishedAt;

    public ActivityJpaEntity() {
    }

    private ActivityJpaEntity(
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final int aType,
            final int aStatus,
            final Instant aScheduled,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aFinishedAt
    ) {
        this.creativeCompanyUserId = aCreativeCompanyUserId;
        this.responsibleCompanyUserId = aResponsibleCompanyUserId;
        this.companyId = aCompanyId;
        this.negotiationId = aNegotiationId;
        this.description = aDescription;
        this.response = aResponse;
        this.type = aType;
        this.status = aStatus;
        this.scheduled = aScheduled;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.finishedAt = aFinishedAt;
    }

    private ActivityJpaEntity(
            final Long anId,
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final int aType,
            final int aStatus,
            final Instant aScheduled,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aFinishedAt
    ) {
        this.id = anId;
        this.creativeCompanyUserId = aCreativeCompanyUserId;
        this.responsibleCompanyUserId = aResponsibleCompanyUserId;
        this.companyId = aCompanyId;
        this.negotiationId = aNegotiationId;
        this.description = aDescription;
        this.response = aResponse;
        this.type = aType;
        this.status = aStatus;
        this.scheduled = aScheduled;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.finishedAt = aFinishedAt;
    }

    public static ActivityJpaEntity from(final Activity anActivity) {
        return new ActivityJpaEntity(
                anActivity.getCreativeCompanyUserId(),
                anActivity.getResponsibleCompanyUserId(),
                anActivity.getCompanyId(),
                anActivity.getNegotiationId(),
                anActivity.getDescription(),
                anActivity.getResponse(),
                anActivity.getType().getId(),
                anActivity.getStatus().getId(),
                anActivity.getScheduled(),
                anActivity.getCreatedAt(),
                anActivity.getUpdatedAt(),
                anActivity.getFinishedAt()
        );
    }

    public static ActivityJpaEntity fromId(final Activity anActivity) {
        return new ActivityJpaEntity(
                Long.parseLong(anActivity.getId().getValue()),
                anActivity.getCreativeCompanyUserId(),
                anActivity.getResponsibleCompanyUserId(),
                anActivity.getCompanyId(),
                anActivity.getNegotiationId(),
                anActivity.getDescription(),
                anActivity.getResponse(),
                anActivity.getType().getId(),
                anActivity.getStatus().getId(),
                anActivity.getScheduled(),
                anActivity.getCreatedAt(),
                anActivity.getUpdatedAt(),
                anActivity.getFinishedAt()
        );
    }

    public Activity toAggregate() {
        return Activity.with(
                ActivityID.from(getId().toString()),
                getCreativeCompanyUserId(),
                getResponsibleCompanyUserId(),
                getCompanyId(),
                getNegotiationId(),
                getDescription(),
                getResponse(),
                ActivityType.getActivityType((short) getType()),
                ActivityStatus.getActivityStatus((short) getStatus()),
                getScheduled(),
                getCreatedAt(),
                getUpdatedAt(),
                getFinishedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreativeCompanyUserId() {
        return creativeCompanyUserId;
    }

    public void setCreativeCompanyUserId(Long creativeCompanyUserId) {
        this.creativeCompanyUserId = creativeCompanyUserId;
    }

    public Long getResponsibleCompanyUserId() {
        return responsibleCompanyUserId;
    }

    public void setResponsibleCompanyUserId(Long responsibleCompanyUserId) {
        this.responsibleCompanyUserId = responsibleCompanyUserId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getNegotiationId() {
        return negotiationId;
    }

    public void setNegotiationId(Long negotiationId) {
        this.negotiationId = negotiationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Instant getScheduled() {
        return scheduled;
    }

    public void setScheduled(Instant scheduled) {
        this.scheduled = scheduled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Instant finishedAt) {
        this.finishedAt = finishedAt;
    }
}
