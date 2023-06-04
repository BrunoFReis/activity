package br.com.powercrm.application.activity.complete;

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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.text.ParseException;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CompleteActivityUseCaseIT {

    @Autowired
    private CompleteActivityUseCase useCase;

    @Autowired
    private CreateActivityUseCase useCaseCreate;

    @Autowired
    private ActivityRepository activityRepository;

    @SpyBean
    private ActivityGateway activityGateway;

    @Test
    public void givenAValidCommand_whenCallCompleteActivity_thenShouldBeOK() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedStatusInt = ActivityStatus.COMPLETED.getId();

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

        final var oldActivity =
                activityRepository.findById(Long.parseLong(actualOutput.id())).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        Assertions.assertEquals(1, activityRepository.count());

        useCase.execute(actualOutput.id());

        Assertions.assertEquals(1, activityRepository.count());

        final var actualActivity =
                activityRepository.findById(Long.parseLong(actualOutput.id())).get();

        Assertions.assertEquals(expectedStatusInt, actualActivity.getStatus());
        Assertions.assertNotNull(actualActivity.getCreatedAt());
        Assertions.assertTrue(oldActivity.getUpdatedAt().isBefore(actualActivity.getUpdatedAt()));
        Assertions.assertNotNull(actualActivity.getFinishedAt());
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
    }

    @Test
    public void givenAInvalidID_whenCallCompleteActivity_thenShouldReturnBeOk() throws ParseException {
        final var expectedId = ActivityID.from("123");
        final var expectedErrorMessage = "Activity with ID 123 was not found";
        final var expectedErrorCount = 1;

        final var actualException = assertThrows(DomainException.class, () ->
                useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}
