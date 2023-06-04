package br.com.powercrm.application.activity.create;

import br.com.powercrm.application.UseCase;
import br.com.powercrm.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateActivityUseCase
        extends UseCase<CreateActivityCommand, Either<Notification, CreateActivityOutput>> {
}
