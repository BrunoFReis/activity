package br.com.powercrm.application.activity.update;

import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;

public record UpdateActivityCommand(
        String id,
        Long creativeCompanyUserId,
        Long responsibleCompanyUserId,
        Long companyId,
        Long negotiationId,
        String description,
        String response,
        ActivityType type,

        ActivityStatus status,
        String scheduleDate
) {

    public static UpdateActivityCommand with(
            final String anId,
            final Long aCreativeCompanyUserId,
            final Long aResponsibleCompanyUserId,
            final Long aCompanyId,
            final Long aNegotiationId,
            final String aDescription,
            final String aResponse,
            final ActivityType aType,
            final ActivityStatus aStatus,
            final String aScheduleDate
    ) {
        return new UpdateActivityCommand(
                anId,
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aStatus,
                aScheduleDate);
    }

}
