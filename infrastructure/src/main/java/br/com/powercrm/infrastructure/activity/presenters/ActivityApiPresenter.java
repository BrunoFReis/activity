package br.com.powercrm.infrastructure.activity.presenters;

import br.com.powercrm.application.activity.retrieve.get.ActivityOutput;
import br.com.powercrm.application.activity.retrieve.list.ActivityListOutput;
import br.com.powercrm.infrastructure.activity.models.ActivityListResponse;
import br.com.powercrm.infrastructure.activity.models.ActivityResponse;

public interface ActivityApiPresenter {

    static ActivityResponse present(final ActivityOutput output) {
        return new ActivityResponse(
                Long.parseLong(output.id().getValue()),
                output.creativeCompanyUserId(),
                output.responsibleCompanyUserId(),
                output.companyId(),
                output.negotiationId(),
                output.description(),
                output.response(),
                output.type(),
                output.status(),
                output.scheduleDate(),
                output.createdAt(),
                output.updatedAt(),
                output.finishAt()
        );
    }

    static ActivityListResponse present(final ActivityListOutput output) {
        return new ActivityListResponse(
                Long.parseLong(output.id().getValue()),
                output.creativeCompanyUserId(),
                output.responsibleCompanyUserId(),
                output.companyId(),
                output.negotiationId(),
                output.description(),
                output.response(),
                output.type(),
                output.status(),
                output.scheduleDate(),
                output.createdAt(),
                output.updatedAt(),
                output.finishAt()
        );
    }
}
