package br.com.powercrm.application.activity.create;

import br.com.powercrm.IntegrationTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateActivityUseCaseIT {

    @Autowired
    private CreateActivityUseCase useCase;

    @Autowired
    private ActivityRepository activityRepository;

    @SpyBean
    private ActivityGateway activityGateway;

    @Test
    public void givenAValidCommand_whenCallCreateActivity_thenShouldReturnActivityId() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedScheduleDate = DateUtils.convertStringDateToInstant("01/01/2023");
        final var expectedTypeInt = ActivityType.NOTE.getId();
        final var expectedStatusInt = ActivityStatus.ACTIVE.getId();

        Assertions.assertEquals(0, activityRepository.count());

        final var aCommand = CreateActivityCommand.with(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, activityRepository.count());

        final var actualActivity =
                activityRepository.findById(Long.parseLong(actualOutput.id())).get();

        Assertions.assertEquals(expectedCreativeCompanyUserId, actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualActivity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualActivity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualActivity.getDescription());
        Assertions.assertEquals(expectedResponse, actualActivity.getResponse());
        Assertions.assertEquals(expectedTypeInt, actualActivity.getType());
        Assertions.assertEquals(expectedStatusInt, actualActivity.getStatus());
        Assertions.assertEquals(expectedScheduleDate, actualActivity.getScheduled());
        Assertions.assertNotNull(actualActivity.getCreatedAt());
        Assertions.assertNotNull(actualActivity.getUpdatedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAInvalidCommand_whenCallCreateActivity_thenShouldReturnDomainException() throws ParseException {
        final Long expectedCreativeCompanyUserId = null;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'creativeCompanyUserId' should not be null";

        Assertions.assertEquals(0, activityRepository.count());

        final var aCommand = CreateActivityCommand.with(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());

        Assertions.assertEquals(0, activityRepository.count());

        verify(activityGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateActivityCommand.with(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(activityGateway).create(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    }
}
