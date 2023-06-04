package br.com.powercrm.infrastructure.api.controllers;

import br.com.powercrm.application.activity.complete.CompleteActivityUseCase;
import br.com.powercrm.application.activity.create.CreateActivityCommand;
import br.com.powercrm.application.activity.create.CreateActivityOutput;
import br.com.powercrm.application.activity.create.CreateActivityUseCase;
import br.com.powercrm.application.activity.delete.DeleteActivityUseCase;
import br.com.powercrm.application.activity.retrieve.get.GetActivityByIdUseCase;
import br.com.powercrm.application.activity.retrieve.list.ListActivitiesUseCase;
import br.com.powercrm.application.activity.update.UpdateActivityCommand;
import br.com.powercrm.application.activity.update.UpdateActivityOutput;
import br.com.powercrm.application.activity.update.UpdateActivityUseCase;
import br.com.powercrm.domain.activity.ActivitySearchQuery;
import br.com.powercrm.domain.pagination.Pagination;
import br.com.powercrm.domain.validation.handler.Notification;
import br.com.powercrm.infrastructure.activity.models.ActivityResponse;
import br.com.powercrm.infrastructure.activity.models.CreateActivityRequest;
import br.com.powercrm.infrastructure.activity.models.UpdateActivityRequest;
import br.com.powercrm.infrastructure.activity.presenters.ActivityApiPresenter;
import br.com.powercrm.infrastructure.api.ActivityAPI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.text.ParseException;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class ActivityController implements ActivityAPI {

    private final CreateActivityUseCase createActivityUseCase;

    private final GetActivityByIdUseCase getActivityByIdUseCase;

    private final UpdateActivityUseCase updateActivityUseCase;

    private final DeleteActivityUseCase deleteActivityUseCase;

    private final CompleteActivityUseCase completeActivityUseCase;

    private final ListActivitiesUseCase listActivitiesUseCase;

    public ActivityController(
            final CreateActivityUseCase createActivityUseCase,
            final GetActivityByIdUseCase getActivityByIdUseCase,
            final UpdateActivityUseCase updateActivityUseCase,
            final DeleteActivityUseCase deleteActivityUseCase,
            final CompleteActivityUseCase completeActivityUseCase,
            final ListActivitiesUseCase listActivitiesUseCase) {
        this.createActivityUseCase = Objects.requireNonNull(createActivityUseCase);
        this.getActivityByIdUseCase = Objects.requireNonNull(getActivityByIdUseCase);
        this.updateActivityUseCase = Objects.requireNonNull(updateActivityUseCase);
        this.deleteActivityUseCase = Objects.requireNonNull(deleteActivityUseCase);
        this.completeActivityUseCase = Objects.requireNonNull(completeActivityUseCase);
        this.listActivitiesUseCase = Objects.requireNonNull(listActivitiesUseCase);
    }

    @Override
    public ResponseEntity<?> createActivity(final CreateActivityRequest input) throws ParseException {
        CreateActivityCommand aCommand = CreateActivityCommand.with(
                input.creativeCompanyUserId(),
                input.responsibleCompanyUserId(),
                input.companyId(),
                input.negotiationId(),
                input.description(),
                input.response(),
                input.type(),
                input.scheduleDate()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateActivityOutput, ResponseEntity<?>> onSucess = output ->
                ResponseEntity.created(URI.create("/activities/" + output.id())).body(output);

        return this.createActivityUseCase.execute(aCommand)
                .fold(onError, onSucess);
    }

    @Override
    public Pagination<?> listActivities(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) throws ParseException {
        return listActivitiesUseCase.execute(new ActivitySearchQuery(page, perPage, search, sort, direction))
                .map(ActivityApiPresenter::present);
    }

    @Override
    public ActivityResponse getById(String id) throws Exception {
        return ActivityApiPresenter.present(this.getActivityByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateActivityRequest input) throws ParseException {
        final var aCommand = UpdateActivityCommand.with(
                id,
                input.creativeCompanyUserId(),
                input.responsibleCompanyUserId(),
                input.companyId(),
                input.negotiationId(),
                input.description(),
                input.response(),
                input.type(),
                input.status(),
                input.scheduleDate()
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateActivityOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;

        return this.updateActivityUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String anId) {
        this.deleteActivityUseCase.execute(anId);
    }

    @Override
    public void completeById(String anId) {
        this.completeActivityUseCase.execute(anId);
    }


}
