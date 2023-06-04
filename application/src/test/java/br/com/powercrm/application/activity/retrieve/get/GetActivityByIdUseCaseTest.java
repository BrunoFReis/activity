package br.com.powercrm.application.activity.retrieve.get;

import br.com.powercrm.application.activity.UseCaseTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.*;
import br.com.powercrm.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetActivityByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetActivityIdUseCase useCase;

    @Mock
    private ActivityGateway activityGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(activityGateway);
    }

    @Test
    public void givenAValidCommand_whenCallGetActivityById_thenShouldReturnActivity() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedTypeString = ActivityType.NOTE.getDescription();
        final var expectedStatusString = ActivityStatus.ACTIVE.getDescription();
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedScheduleDate = DateUtils.convertStringDateToInstant("01/01/2023");

        final var anActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType,
                expectedScheduleDate
        );

        final var expectedId = anActivity.getId();

        when(activityGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(anActivity.clone()));

        final var actualOutput = useCase.execute(expectedId.getValue());

        assertNotNull(actualOutput.id());

        verify(activityGateway, times(1)).findById(eq(expectedId));

        Assertions.assertEquals(expectedCreativeCompanyUserId, actualOutput.creativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualOutput.responsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualOutput.companyId());
        Assertions.assertEquals(expectedNegotiationId, actualOutput.negotiationId());
        Assertions.assertEquals(expectedDescription, actualOutput.description());
        Assertions.assertEquals(expectedResponse, actualOutput.response());
        Assertions.assertEquals(expectedTypeString, actualOutput.type());
        Assertions.assertEquals(expectedStatusString, actualOutput.status());
        Assertions.assertEquals(expectedScheduleDateString, actualOutput.scheduleDate());
        Assertions.assertNotNull(actualOutput.createdAt());
        Assertions.assertNotNull(actualOutput.updatedAt());
        Assertions.assertNull(actualOutput.finishAt());

    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        final var expectedId = ActivityID.from("123");
        final var expectedErrorMessage = "Gateway error";

        when(activityGateway.findById(eq(expectedId)))
                .thenThrow(new IllegalStateException("Gateway error"));

        final var actualException = assertThrows(IllegalStateException.class, () ->
                useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAInvalidID_whenCallGetActivityById_thenShouldReturnNotFoundException() throws ParseException {
        final var expectedId = ActivityID.from("123");
        final var expectedErrorMessage = "Activity with ID 123 was not found";
        final var expectedErrorCount = 1;

        when(activityGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () ->
                useCase.execute(expectedId.getValue()));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}
