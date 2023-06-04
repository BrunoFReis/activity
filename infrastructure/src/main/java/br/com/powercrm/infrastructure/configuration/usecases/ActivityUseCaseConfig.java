package br.com.powercrm.infrastructure.configuration.usecases;

import br.com.powercrm.application.activity.complete.CompleteActivityUseCase;
import br.com.powercrm.application.activity.complete.DefaultCompleteActivityUseCase;
import br.com.powercrm.application.activity.create.CreateActivityUseCase;
import br.com.powercrm.application.activity.create.DefaultCreateActivityUseCase;
import br.com.powercrm.application.activity.delete.DefaultDeleteActivityUseCase;
import br.com.powercrm.application.activity.delete.DeleteActivityUseCase;
import br.com.powercrm.application.activity.retrieve.get.DefaultGetActivityIdUseCase;
import br.com.powercrm.application.activity.retrieve.get.GetActivityByIdUseCase;
import br.com.powercrm.application.activity.retrieve.list.DefaultListActivitiesUseCase;
import br.com.powercrm.application.activity.retrieve.list.ListActivitiesUseCase;
import br.com.powercrm.application.activity.update.DefaultUpdateActivityUseCase;
import br.com.powercrm.application.activity.update.UpdateActivityUseCase;
import br.com.powercrm.domain.activity.ActivityGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityUseCaseConfig {

    private final ActivityGateway activityGateway;

    public ActivityUseCaseConfig(final ActivityGateway activityGateway) {
        this.activityGateway = activityGateway;
    }

    @Bean
    public CreateActivityUseCase createActivityUseCase() {
        return new DefaultCreateActivityUseCase(activityGateway);
    }

    @Bean
    public UpdateActivityUseCase updateActivityUseCase() {
        return new DefaultUpdateActivityUseCase(activityGateway);
    }

    @Bean
    public DeleteActivityUseCase deleteActivityUseCase() {
        return new DefaultDeleteActivityUseCase(activityGateway);
    }

    @Bean
    public CompleteActivityUseCase completeActivityUseCase() {
        return new DefaultCompleteActivityUseCase(activityGateway);
    }

    @Bean
    public GetActivityByIdUseCase getActivityByIdUseCase() {
        return new DefaultGetActivityIdUseCase(activityGateway);
    }

    @Bean
    public ListActivitiesUseCase listActivityUseCase() {
        return new DefaultListActivitiesUseCase(activityGateway);
    }
}
