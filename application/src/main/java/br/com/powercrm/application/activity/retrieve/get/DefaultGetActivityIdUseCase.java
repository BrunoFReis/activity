package br.com.powercrm.application.activity.retrieve.get;

import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.domain.exceptions.NotFoundException;
import br.com.powercrm.domain.validation.Error;

import java.text.ParseException;
import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetActivityIdUseCase extends GetActivityByIdUseCase {

    private final ActivityGateway activityGateway;

    public DefaultGetActivityIdUseCase(final ActivityGateway activityGateway) {
        this.activityGateway = Objects.requireNonNull(activityGateway);
    }

    @Override
    public ActivityOutput execute(final String anIn) throws ParseException {
        final var anActivityID = ActivityID.from(anIn);
        return ActivityOutput.from(this.activityGateway.findById(anActivityID)
                .orElseThrow(notFound(anActivityID)));
    }

    private Supplier<DomainException> notFound(final ActivityID anId) {
        return () -> NotFoundException.with(
                new Error("Activity with ID %s was not found".formatted(anId.getValue())));
    }
}
