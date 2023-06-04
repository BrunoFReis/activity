package br.com.powercrm.application.activity.create;

import br.com.powercrm.domain.activity.ActivityType;

import java.time.Instant;

public record CreateActivityCommand(
        Long creativeCompanyUserId,
        Long responsibleCompanyUserId,
        Long companyId,
        Long negotiationId,
        String description,
        String response,
        ActivityType type,
        String scheduleDate
) {

    public static CreateActivityCommand with(
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final String aScheduleDate
    ) {
        return new CreateActivityCommand(
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aScheduleDate);
    }

}
