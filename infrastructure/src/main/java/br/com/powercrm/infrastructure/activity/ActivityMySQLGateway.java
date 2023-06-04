package br.com.powercrm.infrastructure.activity;

import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.activity.ActivitySearchQuery;
import br.com.powercrm.domain.pagination.Pagination;
import br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static br.com.powercrm.infrastructure.utils.SpecificationUtils.equalsLong;
import static br.com.powercrm.infrastructure.utils.SpecificationUtils.like;

@Service
public class ActivityMySQLGateway implements ActivityGateway {

    private final ActivityRepository activityRepository;

    public ActivityMySQLGateway(final ActivityRepository activityRepository) {
        this.activityRepository = Objects.requireNonNull(activityRepository);
    }

    @Override
    public Activity create(final Activity anActivity) {
        return save(ActivityJpaEntity.from(anActivity));
    }

    @Override
    public void deleteById(Activity anActivity) {
        save(ActivityJpaEntity.fromId(anActivity));
    }

    @Override
    public void completeById(Activity anActivity) {
        save(ActivityJpaEntity.fromId(anActivity));
    }

    @Override
    public Optional<Activity> findById(final ActivityID anId) {
        return this.activityRepository.findById(Long.parseLong(anId.getValue()))
                .map(ActivityJpaEntity::toAggregate);
    }

    @Override
    public Activity update(final Activity anActivity) {
        return save(ActivityJpaEntity.fromId(anActivity));
    }

    @Override
    public Pagination<Activity> findAll(final ActivitySearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var especifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str -> assembleSpecification(str, aQuery.sort())
                )
                .orElse(null);

        final var pageResult =
                this.activityRepository.findAll(Specification.where(especifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(ActivityJpaEntity::toAggregate).toList()
        );
    }

    private static Specification<ActivityJpaEntity> assembleSpecification(String str, String sort) {
        if (sort.equals("description") || sort.equals("response")) {
            return like(sort, str);
        }

        return equalsLong(sort, str);
    }


    private Activity save(ActivityJpaEntity anActivity) {
        return activityRepository.save(anActivity)
                .toAggregate();
    }
}
