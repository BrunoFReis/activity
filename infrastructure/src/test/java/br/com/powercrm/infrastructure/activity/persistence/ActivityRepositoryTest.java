package br.com.powercrm.infrastructure.activity.persistence;

import br.com.powercrm.domain.activity.Activity;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;

@MySQLGatewayTest
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    public void givenAnInvalidNullCreativeCompanyUserId_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.creativeCompanyUserId";
        final var expectedPropertyName = "creativeCompanyUserId";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setCreativeCompanyUserId(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullResponsibleCompanyUserId_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.responsibleCompanyUserId";
        final var expectedPropertyName = "responsibleCompanyUserId";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setResponsibleCompanyUserId(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCompanyId_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.companyId";
        final var expectedPropertyName = "companyId";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setCompanyId(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullNegotiationId_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.negotiationId";
        final var expectedPropertyName = "negotiationId";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setNegotiationId(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullDescription_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.description";
        final var expectedPropertyName = "description";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setDescription(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullScheduled_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.scheduled";
        final var expectedPropertyName = "scheduled";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setScheduled(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.createdAt";
        final var expectedPropertyName = "createdAt";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setCreatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedMessage = "not-null property references a null or transient value : br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity.updatedAt";
        final var expectedPropertyName = "updatedAt";
        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                Instant.now()
        );

        final var anEntity = ActivityJpaEntity.from(anActivity);
        anEntity.setUpdatedAt(null);

        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> activityRepository.save(anEntity));

        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}