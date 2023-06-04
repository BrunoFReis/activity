package br.com.powercrm.application.activity.update;

import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityID;

public record UpdateActivityOutput(
        String id
) {

    public static UpdateActivityOutput from(final String anId) {
        return new UpdateActivityOutput(anId);
    }

    public static UpdateActivityOutput from(final Activity anActivity) {
        return new UpdateActivityOutput(anActivity.getId().getValue());
    }
}
