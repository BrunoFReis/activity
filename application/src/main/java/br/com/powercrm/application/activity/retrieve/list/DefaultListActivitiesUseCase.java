package br.com.powercrm.application.activity.retrieve.list;

import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivitySearchQuery;
import br.com.powercrm.domain.pagination.Pagination;

import java.text.ParseException;
import java.util.Objects;

public class DefaultListActivitiesUseCase extends ListActivitiesUseCase {

    private final ActivityGateway activityGateway;

    public DefaultListActivitiesUseCase(final ActivityGateway activityGateway) {
        this.activityGateway = Objects.requireNonNull(activityGateway);
    }

    @Override
    public Pagination<ActivityListOutput> execute(ActivitySearchQuery aQuery) throws ParseException {
        return this.activityGateway.findAll(aQuery)
                .map(ActivityListOutput::from);
    }
}
