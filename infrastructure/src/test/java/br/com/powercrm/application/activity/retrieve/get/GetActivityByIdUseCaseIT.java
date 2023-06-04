package br.com.powercrm.application.activity.retrieve.get;

import br.com.powercrm.IntegrationTest;
import br.com.powercrm.application.activity.create.CreateActivityCommand;
import br.com.powercrm.application.activity.create.CreateActivityUseCase;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.*;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class GetActivityByIdUseCaseIT {

    @Autowired
    private GetActivityByIdUseCase useCase;

    @Autowired
    private CreateActivityUseCase useCaseCreate;

    @Autowired
    private ActivityRepository activityRepository;

    @SpyBean
    private ActivityGateway activityGateway;

    @Test
    public void givenAValidCommand_whenCallGetActivityById_thenShouldReturnActivity() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedTypeString = ActivityType.NOTE.getDescription();
        final var expectedStatusString = ActivityStatus.ACTIVE.getDescription();

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

        final var actualOutput = useCaseCreate.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, activityRepository.count());

        final var actualActivity = useCase.execute(actualOutput.id());

        Assertions.assertEquals(expectedCreativeCompanyUserId, actualActivity.creativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualActivity.responsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualActivity.companyId());
        Assertions.assertEquals(expectedNegotiationId, actualActivity.negotiationId());
        Assertions.assertEquals(expectedDescription, actualActivity.description());
        Assertions.assertEquals(expectedResponse, actualActivity.response());
        Assertions.assertEquals(expectedTypeString, actualActivity.type());
        Assertions.assertEquals(expectedStatusString, actualActivity.status());
        Assertions.assertEquals(expectedScheduleDateString, actualActivity.scheduleDate());
        Assertions.assertNotNull(actualActivity.createdAt());
        Assertions.assertNotNull(actualActivity.updatedAt());
        Assertions.assertNull(actualActivity.finishAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        final var expectedId = ActivityID.from("123");
        final var expectedErrorMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(activityGateway).findById(any());

        final var actualException = assertThrows(IllegalStateException.class, () ->
                useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(activityGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    public void givenAInvalidID_whenCallGetActivityById_thenShouldReturnNotFoundException() throws ParseException {
        final var expectedId = ActivityID.from("123");
        final var expectedErrorMessage = "Activity with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(DomainException.class, () ->
                useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}
