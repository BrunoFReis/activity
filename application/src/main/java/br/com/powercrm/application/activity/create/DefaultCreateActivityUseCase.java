package br.com.powercrm.application.activity.create;

import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.text.ParseException;
import java.util.Objects;

import static io.vavr.API.Try;
import static io.vavr.control.Either.left;

public class DefaultCreateActivityUseCase extends CreateActivityUseCase {

    private final ActivityGateway activityGateway;

    public DefaultCreateActivityUseCase(final ActivityGateway activityGateway) {
        this.activityGateway = Objects.requireNonNull(activityGateway);
    }


    @Override
    public Either<Notification, CreateActivityOutput> execute(final CreateActivityCommand aCommand) throws ParseException {
        final var aCreativeCompanyUserId = aCommand.creativeCompanyUserId();
        final var aResponsibleCompanyUserId = aCommand.responsibleCompanyUserId();
        final var aCompanyId = aCommand.companyId();
        final var aNegotiationId = aCommand.negotiationId();
        final var aDescription = aCommand.description();
        final var aResponse = aCommand.response();
        final var aType = aCommand.type();
        final var aScheduleDate = DateUtils.convertStringDateToInstant(aCommand.scheduleDate());

        final var notification = Notification.create();

        final var anActivity = Activity.newActivity(
                aCreativeCompanyUserId,
                aResponsibleCompanyUserId,
                aCompanyId,
                aNegotiationId,
                aDescription,
                aResponse,
                aType,
                aScheduleDate
        );
        anActivity.validate(notification);

        return notification.hasError() ? left(notification) : create(anActivity);
    }

    private Either<Notification, CreateActivityOutput> create(final Activity anActivity) {
        return Try(() -> this.activityGateway.create(anActivity))
                .toEither()
                .bimap(Notification::create, CreateActivityOutput::from);
    }
}
