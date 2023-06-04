package br.com.powercrm.application.activity.retrieve.list;

import br.com.powercrm.application.UseCase;
import br.com.powercrm.domain.activity.ActivitySearchQuery;
import br.com.powercrm.domain.pagination.Pagination;

public abstract class ListActivitiesUseCase extends UseCase<ActivitySearchQuery, Pagination<ActivityListOutput>> {
}
