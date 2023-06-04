package br.com.powercrm.application.activity.update;

import br.com.powercrm.application.UseCase;
import br.com.powercrm.application.activity.create.CreateActivityCommand;
import br.com.powercrm.application.activity.create.CreateActivityOutput;
import br.com.powercrm.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateActivityUseCase
        extends UseCase<UpdateActivityCommand, Either<Notification, UpdateActivityOutput>> {
}
