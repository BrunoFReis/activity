package br.com.powercrm.domain.activity;

import br.com.powercrm.domain.pagination.Pagination;

import java.util.Optional;

public interface ActivityGateway {

    Activity create(Activity anActivity);

    void deleteById(Activity anActivity);

    void completeById(Activity anActivity);

    Optional<Activity> findById(ActivityID anId);

    Activity update(Activity anActivity);

    Pagination<Activity> findAll(ActivitySearchQuery aQuery);
}
