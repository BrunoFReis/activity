package br.com.powercrm.application.activity.delete;

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
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeleteActivityUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteActivityUseCase useCase;

    @Mock
    private ActivityGateway activityGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(activityGateway);
    }

    @Test
    public void givenAValidCommand_whenCallDeleteActivity_thenShouldBeOK() throws ParseException {
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var expectedStatus = ActivityStatus.CANCELED;

        final var expectedId = anActivity.getId();

        when(activityGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(anActivity.clone()));

        doNothing()
                .when(activityGateway).deleteById(eq(anActivity));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(activityGateway, times(1)).findById(eq(expectedId));

        verify(activityGateway, times(1))
                .deleteById(argThat(aDeleteActivity ->
                        Objects.equals(expectedStatus, aDeleteActivity.getStatus())
                                && anActivity.getUpdatedAt().isBefore(aDeleteActivity.getUpdatedAt())
                                && Objects.isNull(aDeleteActivity.getFinishedAt())
                ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldAException() throws ParseException {
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );
        final var expectedStatus = ActivityStatus.CANCELED;

        final var expectedId = anActivity.getId();

        when(activityGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(anActivity.clone()));

        doThrow(new IllegalStateException("Gateway error"))
                .when(activityGateway).deleteById(eq(anActivity));

        Assertions.assertThrows(IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue()));

        verify(activityGateway, times(1)).findById(eq(expectedId));

        verify(activityGateway, times(1))
                .deleteById(argThat(aDeleteActivity ->
                        Objects.equals(expectedStatus, aDeleteActivity.getStatus())
                                && anActivity.getUpdatedAt().isBefore(aDeleteActivity.getUpdatedAt())
                                && Objects.isNull(aDeleteActivity.getFinishedAt())
                ));
    }

    @Test
    public void givenAInvalidID_whenCallDeleteActivity_thenShouldReturnBeOk() throws ParseException {
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
