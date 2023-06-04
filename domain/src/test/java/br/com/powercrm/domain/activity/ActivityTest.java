package br.com.powercrm.domain.activity;

import br.com.powercrm.domain.UnitTest;
import br.com.powercrm.domain.exceptions.DomainException;
import br.com.powercrm.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class ActivityTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallNewActivity_thenInstantiateanActivity() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDate = Instant.now();

        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        Assertions.assertNotNull(actualActivity);
        Assertions.assertNotNull(actualActivity.getId());
        Assertions.assertEquals(expectedCreativeCompanyUserId, actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualActivity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualActivity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualActivity.getDescription());
        Assertions.assertEquals(expectedResponse, actualActivity.getResponse());
        Assertions.assertEquals(expectedType, actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(expectedScheduleDate, actualActivity.getScheduled());
        Assertions.assertNotNull(actualActivity.getCreatedAt());
        Assertions.assertNotNull(actualActivity.getUpdatedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAnInvalidCreativeCompanyUser_whenCallNewActivity_thenShouldReceiveError() {
        final Long expectedCreativeCompanyUserId = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'creativeCompanyUserId' should not be null";

        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidResponsibleCompanyUserId_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final Long expectedResponsibleCompanyUserId = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'responsibleCompanyUserId' should not be null";

        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidCompanyId_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final Long expectedCompanyId = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'companyId' should not be null";

        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNegotiationId_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final Long expectedNegotiationId = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'negotiationId' should not be null";

        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyDescription_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final String expectedDescription = "   ";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidDescriptionLengthLessThan3_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final String expectedDescription = "ab ";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 3 and 255 characters";

        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidDescriptionLengthMoreThan255_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final String expectedDescription = """
                Percebemos, cada vez mais, que a constante divulgação das informações apresenta tendências no sentido
                de aprovar a manutenção do fluxo de informações. É claro que o novo modelo estrutural aqui preconizado
                estimula a padronização das diretrizes de desenvolvimento para o futuro. Nunca é demais lembrar o.              
                """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 3 and 255 characters";

        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidType_whenCallNewActivity_thenShouldReceiveError() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final ActivityType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var expectedScheduleDate = Instant.now();


        final var actualActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var actualException =
                Assertions.assertThrows(DomainException.class,
                        () -> actualActivity.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidActivity_whenCallCancel_thenReturnActivityCanceled() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDate = Instant.now();

        final var anActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var cancelUpdatedAt = anActivity.getUpdatedAt();
        Assertions.assertEquals(ActivityStatus.ACTIVE, anActivity.getStatus());

        final var cancelActivity = anActivity.canceled();

        Assertions.assertDoesNotThrow(
                () -> cancelActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), cancelActivity.getId());
        Assertions.assertEquals(anActivity.getCreativeCompanyUserId(), cancelActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(anActivity.getResponsibleCompanyUserId(), cancelActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(anActivity.getCompanyId(), cancelActivity.getCompanyId());
        Assertions.assertEquals(anActivity.getNegotiationId(), cancelActivity.getNegotiationId());
        Assertions.assertEquals(anActivity.getDescription(), cancelActivity.getDescription());
        Assertions.assertEquals(anActivity.getResponse(), cancelActivity.getResponse());
        Assertions.assertEquals(anActivity.getType(), cancelActivity.getType());
        Assertions.assertEquals(ActivityStatus.CANCELED, cancelActivity.getStatus());
        Assertions.assertEquals(anActivity.getScheduled(), cancelActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), cancelActivity.getCreatedAt());
        Assertions.assertTrue(cancelActivity.getUpdatedAt().isAfter(cancelUpdatedAt));
        Assertions.assertNull(cancelActivity.getFinishedAt());

        final var updatedAt = cancelActivity.getUpdatedAt();

        final var actualActivity = cancelActivity.activated();

        Assertions.assertDoesNotThrow(
                () -> actualActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), actualActivity.getId());
        Assertions.assertEquals(anActivity.getCreativeCompanyUserId(), actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(anActivity.getResponsibleCompanyUserId(), actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(anActivity.getCompanyId(), actualActivity.getCompanyId());
        Assertions.assertEquals(anActivity.getNegotiationId(), actualActivity.getNegotiationId());
        Assertions.assertEquals(anActivity.getDescription(), actualActivity.getDescription());
        Assertions.assertEquals(anActivity.getResponse(), actualActivity.getResponse());
        Assertions.assertEquals(anActivity.getType(), actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(anActivity.getScheduled(), actualActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualActivity.getCreatedAt());
        Assertions.assertTrue(actualActivity.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualActivity.getFinishedAt());


    }

    @Test
    public void givenAValidCanceledActivity_whenCallActivate_thenReturnActivityActivated() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.CANCELED;
        final var expectedScheduleDate = Instant.now();

        final var anActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var updatedAt = anActivity.getUpdatedAt();
        Assertions.assertEquals(ActivityStatus.ACTIVE, anActivity.getStatus());

        final var actualActivity = anActivity.canceled();

        Assertions.assertDoesNotThrow(
                () -> actualActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), actualActivity.getId());
        Assertions.assertEquals(anActivity.getCreativeCompanyUserId(), actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(anActivity.getResponsibleCompanyUserId(), actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(anActivity.getCompanyId(), actualActivity.getCompanyId());
        Assertions.assertEquals(anActivity.getNegotiationId(), actualActivity.getNegotiationId());
        Assertions.assertEquals(anActivity.getDescription(), actualActivity.getDescription());
        Assertions.assertEquals(anActivity.getResponse(), actualActivity.getResponse());
        Assertions.assertEquals(anActivity.getType(), actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(anActivity.getScheduled(), actualActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualActivity.getCreatedAt());
        Assertions.assertTrue(actualActivity.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAValidActivity_whenCallCompleted_thenReturnActivityActivated() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.COMPLETED;
        final var expectedScheduleDate = Instant.now();

        final var anActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        final var updatedAt = anActivity.getUpdatedAt();
        Assertions.assertEquals(ActivityStatus.ACTIVE, anActivity.getStatus());
        Assertions.assertNull(anActivity.getFinishedAt());

        final var actualActivity = anActivity.completed();

        Assertions.assertDoesNotThrow(
                () -> actualActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), actualActivity.getId());
        Assertions.assertEquals(anActivity.getCreativeCompanyUserId(), actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(anActivity.getResponsibleCompanyUserId(), actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(anActivity.getCompanyId(), actualActivity.getCompanyId());
        Assertions.assertEquals(anActivity.getNegotiationId(), actualActivity.getNegotiationId());
        Assertions.assertEquals(anActivity.getDescription(), actualActivity.getDescription());
        Assertions.assertEquals(anActivity.getResponse(), actualActivity.getResponse());
        Assertions.assertEquals(anActivity.getType(), actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(anActivity.getScheduled(), actualActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualActivity.getCreatedAt());
        Assertions.assertTrue(actualActivity.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAValidActivity_whenCallUpdate_thenReturnActivityUpdated() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDate = Instant.now().plusMillis(1000);

        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "update", "update", ActivityType.CALL, Instant.now()
        );

        final var updatedAt = anActivity.getUpdatedAt();
        Assertions.assertEquals(ActivityStatus.ACTIVE, anActivity.getStatus());

        final var actualActivity = anActivity.update(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, ActivityStatus.ACTIVE,
                expectedScheduleDate
        );

        Assertions.assertDoesNotThrow(
                () -> actualActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), actualActivity.getId());
        Assertions.assertEquals(expectedCreativeCompanyUserId, actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualActivity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualActivity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualActivity.getDescription());
        Assertions.assertEquals(expectedResponse, actualActivity.getResponse());
        Assertions.assertEquals(expectedType, actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(expectedScheduleDate, actualActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualActivity.getCreatedAt());
        Assertions.assertTrue(actualActivity.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAValidActivity_whenCallUpdate_thenReturnActivityUpdatedCanceled() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.CANCELED;
        final var expectedScheduleDate = Instant.now().plusMillis(1000);

        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "update", "update", ActivityType.CALL, Instant.now()
        );

        final var updatedAt = anActivity.getUpdatedAt();
        Assertions.assertEquals(ActivityStatus.ACTIVE, anActivity.getStatus());

        final var actualActivity = anActivity.update(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, ActivityStatus.CANCELED,
                expectedScheduleDate
        );

        Assertions.assertDoesNotThrow(
                () -> actualActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), actualActivity.getId());
        Assertions.assertEquals(expectedCreativeCompanyUserId, actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualActivity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualActivity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualActivity.getDescription());
        Assertions.assertEquals(expectedResponse, actualActivity.getResponse());
        Assertions.assertEquals(expectedType, actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(expectedScheduleDate, actualActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualActivity.getCreatedAt());
        Assertions.assertTrue(actualActivity.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenAValidActivity_whenCallUpdate_thenReturnActivityUpdatedCompleted() {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.COMPLETED;
        final var expectedScheduleDate = Instant.now().plusMillis(1000);

        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "update", "update", ActivityType.CALL, Instant.now()
        );

        final var updatedAt = anActivity.getUpdatedAt();
        Assertions.assertEquals(ActivityStatus.ACTIVE, anActivity.getStatus());
        Assertions.assertNull(anActivity.getFinishedAt());

        final var actualActivity = anActivity.update(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, ActivityStatus.COMPLETED,
                expectedScheduleDate
        );

        Assertions.assertDoesNotThrow(
                () -> actualActivity.validate(new ThrowsValidationHandler()));
        Assertions.assertEquals(anActivity.getId(), actualActivity.getId());
        Assertions.assertEquals(expectedCreativeCompanyUserId, actualActivity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualActivity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualActivity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualActivity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualActivity.getDescription());
        Assertions.assertEquals(expectedResponse, actualActivity.getResponse());
        Assertions.assertEquals(expectedType, actualActivity.getType());
        Assertions.assertEquals(expectedStatus, actualActivity.getStatus());
        Assertions.assertEquals(expectedScheduleDate, actualActivity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualActivity.getCreatedAt());
        Assertions.assertTrue(actualActivity.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualActivity.getFinishedAt());
    }
}