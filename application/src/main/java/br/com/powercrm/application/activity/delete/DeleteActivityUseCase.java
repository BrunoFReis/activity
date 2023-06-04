package br.com.powercrm.application.activity.delete;

import br.com.powercrm.application.UnitUseCase;
import br.com.powercrm.application.UseCase;
import br.com.powercrm.application.activity.update.UpdateActivityCommand;
import br.com.powercrm.application.activity.update.UpdateActivityOutput;
import br.com.powercrm.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class DeleteActivityUseCase
        extends UnitUseCase<String> {
}
