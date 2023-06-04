package br.com.powercrm.application.activity.create;

import br.com.powercrm.application.activity.UseCaseTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.ActivityGateway;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class CreateActivityUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateActivityUseCase useCase;

    @Mock
    private ActivityGateway activityGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(activityGateway);
    }

    @Test
    public void givenAValidCommand_whenCallCreateActivity_thenShouldReturnActivityId() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedScheduleDate = DateUtils.convertStringDateToInstant("01/01/2023");

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

        when(activityGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(activityGateway, times(1))
                .create(argThat(anActivity ->
                        Objects.equals(expectedCreativeCompanyUserId, anActivity.getCreativeCompanyUserId())
                                && Objects.nonNull(anActivity.getId())
                                && Objects.equals(expectedResponsibleCompanyUserId, anActivity.getResponsibleCompanyUserId())
                                && Objects.equals(expectedCompanyId, anActivity.getCompanyId())
                                && Objects.equals(expectedNegotiationId, anActivity.getNegotiationId())
                                && Objects.equals(expectedDescription, anActivity.getDescription())
                                && Objects.equals(expectedResponse, anActivity.getResponse())
                                && Objects.equals(expectedType, anActivity.getType())
                                && Objects.equals(expectedStatus, anActivity.getStatus())
                                && Objects.equals(expectedScheduleDate, anActivity.getScheduled())
                                && Objects.nonNull(anActivity.getCreatedAt())
                                && Objects.nonNull(anActivity.getUpdatedAt())
                                && Objects.isNull(anActivity.getFinishedAt())
                ));
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
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedScheduleDate = DateUtils.convertStringDateToInstant("01/01/2023");
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

        when(activityGateway.create(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(activityGateway, times(1)).create(argThat(anActivity ->
                Objects.equals(expectedCreativeCompanyUserId, anActivity.getCreativeCompanyUserId())
                        && Objects.nonNull(anActivity.getId())
                        && Objects.equals(expectedResponsibleCompanyUserId, anActivity.getResponsibleCompanyUserId())
                        && Objects.equals(expectedCompanyId, anActivity.getCompanyId())
                        && Objects.equals(expectedNegotiationId, anActivity.getNegotiationId())
                        && Objects.equals(expectedDescription, anActivity.getDescription())
                        && Objects.equals(expectedResponse, anActivity.getResponse())
                        && Objects.equals(expectedType, anActivity.getType())
                        && Objects.equals(expectedStatus, anActivity.getStatus())
                        && Objects.equals(expectedScheduleDate, anActivity.getScheduled())
                        && Objects.nonNull(anActivity.getCreatedAt())
                        && Objects.nonNull(anActivity.getUpdatedAt())
                        && Objects.isNull(anActivity.getFinishedAt())));
    }
}
