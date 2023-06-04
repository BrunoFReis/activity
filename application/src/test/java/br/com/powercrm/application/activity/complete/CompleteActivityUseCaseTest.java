package br.com.powercrm.application.activity.complete;

import br.com.powercrm.application.activity.UseCaseTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.*;
import br.com.powercrm.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CompleteActivityUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCompleteActivityUseCase useCase;

    @Mock
    private ActivityGateway activityGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(activityGateway);
    }

    @Test
    public void givenAValidCommand_whenCallCompleteActivity_thenShouldBeOK() throws ParseException {
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var expectedStatus = ActivityStatus.COMPLETED;

        final var expectedId = anActivity.getId();

        when(activityGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(anActivity.clone()));

        doNothing()
                .when(activityGateway).completeById(eq(anActivity));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(activityGateway, times(1)).findById(eq(expectedId));

        verify(activityGateway, times(1))
                .completeById(argThat(aDeleteActivity ->
                        Objects.equals(expectedStatus, aDeleteActivity.getStatus())
                                && anActivity.getUpdatedAt().isBefore(aDeleteActivity.getUpdatedAt())
                                && Objects.nonNull(aDeleteActivity.getFinishedAt())
                ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );
        final var expectedStatus = ActivityStatus.COMPLETED;

        final var expectedId = anActivity.getId();

        when(activityGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(anActivity.clone()));

        doThrow(new IllegalStateException("Gateway error"))
                .when(activityGateway).completeById(eq(anActivity));

        Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        verify(activityGateway, times(1)).findById(eq(expectedId));

        verify(activityGateway, times(1))
                .completeById(argThat(aDeleteActivity ->
                        Objects.equals(expectedStatus, aDeleteActivity.getStatus())
                                && anActivity.getUpdatedAt().isBefore(aDeleteActivity.getUpdatedAt())
                                && Objects.nonNull(aDeleteActivity.getFinishedAt())
                ));
    }

    @Test
    public void givenAInvalidID_whenCallCompleteActivity_thenShouldReturnBeOk() throws ParseException {
        final var expectedErrorCount = 1;
        final var expectedId = "123";
        final var expectedErrorMessage = "Activity with ID %s was not found".formatted(expectedId);

        when(activityGateway.findById(eq(ActivityID.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () ->
                useCase.execute(expectedId));

        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(activityGateway, times(1)).findById(eq(ActivityID.from(expectedId)));
        verify(activityGateway, times(0)).deleteById(any());
    }
}
