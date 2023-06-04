package br.com.powercrm.application.activity.update;

import br.com.powercrm.IntegrationTest;
import br.com.powercrm.application.activity.create.CreateActivityCommand;
import br.com.powercrm.application.activity.create.CreateActivityUseCase;
import br.com.powercrm.application.activity.delete.DeleteActivityUseCase;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.*;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateActivityUseCaseIT {

    @Autowired
    private UpdateActivityUseCase useCase;

    @Autowired
    private CreateActivityUseCase useCaseCreate;

    @Autowired
    private ActivityRepository activityRepository;

    @SpyBean
    private ActivityGateway activityGateway;

    @Test
    public void givenAValidCommand_whenCallUpdateActivity_thenShouldReturnActivityId() throws ParseException {
        Assertions.assertEquals(0, activityRepository.count());

        final var aCommandCreate = CreateActivityCommand.with(
                1L,
                1L,
                1L,
                1L,
                "teste",
                "teste",
                ActivityType.EMAIL,
                "01/01/2023"
        );

        final var oldOutput = useCaseCreate.execute(aCommandCreate).get();

        Assertions.assertEquals(1, activityRepository.count());

        final var oldActivity =
                activityRepository.findById(Long.parseLong(oldOutput.id())).get();

        Assertions.assertEquals(1L, oldActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(1L, oldActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(1L, oldActivity.getCompanyId());
        Assertions.assertEquals(1L, oldActivity.getNegotiationId());
        Assertions.assertEquals("teste", oldActivity.getDescription());
        Assertions.assertEquals("teste", oldActivity.getResponse());
        Assertions.assertEquals(ActivityType.EMAIL.getId(), oldActivity.getType());
        Assertions.assertEquals(ActivityStatus.ACTIVE.getId(), oldActivity.getStatus());
        Assertions.assertEquals(DateUtils.convertStringDateToInstant("01/01/2023"), oldActivity.getScheduled());
        Assertions.assertNotNull(oldActivity.getCreatedAt());
        Assertions.assertNotNull(oldActivity.getUpdatedAt());
        Assertions.assertNull(oldActivity.getFinishedAt());

        final var expectedCreativeCompanyUserId = 2L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 2L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.CANCELED;
        final var expectedTypeInt = ActivityType.NOTE.getId();
        final var expectedStatusInt = ActivityStatus.CANCELED.getId();
        final var expectedScheduleDateString = "02/01/2023";

        final var expectedId = oldOutput.id();

        final var aCommand = UpdateActivityCommand.with(
                expectedId,
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedStatus,
                expectedScheduleDateString
        );

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, activityRepository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

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
        Assertions.assertEquals(DateUtils.convertStringDateToInstant(expectedScheduleDateString), actualActivity.getScheduled());
        Assertions.assertNotNull(actualActivity.getCreatedAt());
        Assertions.assertNotNull(actualActivity.getUpdatedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAInvalidCommand_whenCallUpdateActivity_thenShouldReturnDomainException() throws ParseException {
        Assertions.assertEquals(0, activityRepository.count());

        final var aCommandCreate = CreateActivityCommand.with(
                1L,
                1L,
                1L,
                1L,
                "teste",
                "teste",
                ActivityType.EMAIL,
                "01/01/2023"
        );

        final var oldOutput = useCaseCreate.execute(aCommandCreate).get();

        Assertions.assertEquals(1, activityRepository.count());

        final Long expectedCreativeCompanyUserId = null;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'creativeCompanyUserId' should not be null";

        final var expectedId = oldOutput.id();

        final var aCommand = UpdateActivityCommand.with(
                expectedId,
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedStatus,
                expectedScheduleDateString
        );

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(activityGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        Assertions.assertEquals(0, activityRepository.count());

        final var aCommandCreate = CreateActivityCommand.with(
                1L,
                1L,
                1L,
                1L,
                "teste",
                "teste",
                ActivityType.EMAIL,
                "01/01/2023"
        );

        final var oldOutput = useCaseCreate.execute(aCommandCreate).get();

        final var oldActivity =
                activityRepository.findById(Long.parseLong(oldOutput.id())).get();

        Assertions.assertEquals(1, activityRepository.count());

        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var expectedId = oldOutput.id();

        final var aCommand = UpdateActivityCommand.with(
                expectedId,
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedStatus,
                expectedScheduleDateString
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(activityGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualActivity =
                activityRepository.findById(Long.parseLong(expectedId)).get();

        Assertions.assertEquals(oldActivity.getCreativeCompanyUserId(), actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(oldActivity.getResponsibleCompanyUserId(), actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(oldActivity.getCompanyId(), actualActivity.getCompanyId());
        Assertions.assertEquals(oldActivity.getNegotiationId(), actualActivity.getNegotiationId());
        Assertions.assertEquals(oldActivity.getDescription(), actualActivity.getDescription());
        Assertions.assertEquals(oldActivity.getResponse(), actualActivity.getResponse());
        Assertions.assertEquals(oldActivity.getType(), actualActivity.getType());
        Assertions.assertEquals(oldActivity.getStatus(), actualActivity.getStatus());
        Assertions.assertEquals(oldActivity.getScheduled(), actualActivity.getScheduled());
        Assertions.assertNotNull(actualActivity.getCreatedAt());
        Assertions.assertNotNull(actualActivity.getUpdatedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAInvalidID_whenCallUpdateActivity_thenShouldReturnNotFoundException() throws ParseException {
        final Long expectedCreativeCompanyUserId = null;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedId = "123";
        final var expectedErrorMessage = "Activity with ID %s was not found".formatted(expectedId);

        final var aCommand = UpdateActivityCommand.with(
                expectedId,
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedStatus,
                expectedScheduleDateString
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
