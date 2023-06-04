package br.com.powercrm.application.activity.update;

import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.domain.exceptions.NotFoundException;
import br.com.powercrm.domain.validation.Error;
import br.com.powercrm.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.text.ParseException;
import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Try;
import static io.vavr.control.Either.left;

public class DefaultUpdateActivityUseCase extends UpdateActivityUseCase {

    private final ActivityGateway activityGateway;

    public DefaultUpdateActivityUseCase(final ActivityGateway activityGateway) {
        this.activityGateway = Objects.requireNonNull(activityGateway);
    }


    @Override
    public Either<Notification, UpdateActivityOutput> execute(final UpdateActivityCommand aCommand) throws ParseException {
        final var anId = ActivityID.from(aCommand.id());
        final var aCreativeCompanyUserId = aCommand.creativeCompanyUserId();
        final var aResponsibleCompanyUserId = aCommand.responsibleCompanyUserId();
        final var aCompanyId = aCommand.companyId();
        final var aNegotiationId = aCommand.negotiationId();
        final var aDescription = aCommand.description();
        final var aResponse = aCommand.response();
        final var aType = aCommand.type();
        final var aStatus = aCommand.status();
        final var aScheduleDate = DateUtils.convertStringDateToInstant(aCommand.scheduleDate());

        final var anActivity = this.activityGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();

        anActivity
                .update(
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aStatus,
                aScheduleDate
            ).validate(notification);

        return notification.hasError() ? left(notification) : update(anActivity);
    }

    private Supplier<DomainException> notFound(final ActivityID anId) {
        return () -> NotFoundException.with(
                new Error("Activity with ID %s was not found".formatted(anId.getValue())));
    }

    private Either<Notification, UpdateActivityOutput> update(final Activity anActivity) {
        return Try(() -> this.activityGateway.update(anActivity))
                .toEither()
                .bimap(Notification::create, UpdateActivityOutput::from);
    }
}
