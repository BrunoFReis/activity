package br.com.powercrm.application.activity.create;

import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityID;

public record CreateActivityOutput(
        String id
) {

    public static CreateActivityOutput from(final String anId) {
        return new CreateActivityOutput(anId);
    }

    public static CreateActivityOutput from(final Activity anActivity) {
        return new CreateActivityOutput(anActivity.getId().getValue());
    }
}
