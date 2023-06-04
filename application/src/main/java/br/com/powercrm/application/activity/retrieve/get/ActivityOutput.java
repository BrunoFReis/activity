package br.com.powercrm.application.activity.retrieve.get;

import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityID;

public record ActivityOutput(
        ActivityID id,
        Long creativeCompanyUserId,
        Long responsibleCompanyUserId,
        Long companyId,
        Long negotiationId,
        String description,
        String response,
        String type,
        String status,
        String scheduleDate,
        String createdAt,
        String updatedAt,
        String finishAt
) {

    public static ActivityOutput from(final Activity anActivity) {
        return new ActivityOutput(
                anActivity.getId(),
                anActivity.getCreativeCompanyUserId(),
                anActivity.getResponsibleCompanyUserId(),
                anActivity.getCompanyId(),
                anActivity.getNegotiationId(),
                anActivity.getDescription(),
                anActivity.getResponse(),
                anActivity.getType().getDescription(),
                anActivity.getStatus().getDescription(),
                DateUtils.convertInstantToStringDate(anActivity.getScheduled()),
                DateUtils.convertInstantToStringDateTime(anActivity.getCreatedAt()),
                DateUtils.convertInstantToStringDateTime(anActivity.getUpdatedAt()),
                DateUtils.convertInstantToStringDateTime(anActivity.getFinishedAt()));
    }
}
