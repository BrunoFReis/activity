package br.com.powercrm.application.activity.delete;

import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.domain.exceptions.NotFoundException;
import br.com.powercrm.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultDeleteActivityUseCase extends DeleteActivityUseCase {

    private final ActivityGateway activityGateway;

    public DefaultDeleteActivityUseCase(final ActivityGateway activityGateway) {
        this.activityGateway = Objects.requireNonNull(activityGateway);
    }

    @Override
    public void execute(final String anId) {
        final var activityID = ActivityID.from(anId);

        final var anActivity = this.activityGateway.findById(activityID)
                .orElseThrow(notFound(activityID));

        anActivity.canceled();

        this.activityGateway.deleteById(anActivity);
    }

    private Supplier<DomainException> notFound(final ActivityID anId) {
        return () -> NotFoundException.with(
                new Error("Activity with ID %s was not found".formatted(anId.getValue())));
    }
}
