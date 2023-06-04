package br.com.powercrm.infrastructure.activity;

import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.*;
import br.com.powercrm.MySQLGatewayTest;
import br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.text.ParseException;
import java.time.Instant;
import java.util.List;

@MySQLGatewayTest
public class ActivityMySQLGatewayTest {

    @Autowired
    private ActivityMySQLGateway activityMySQLGateway;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    public void givenAValidActivity_whenCallsCreate_shoudReturnANewActivity() throws ParseException {
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

        Assertions.assertEquals(0, activityRepository.count());

        final var actualActivity = activityMySQLGateway.create(anActivity);

        Assertions.assertEquals(1, activityRepository.count());

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
        Assertions.assertEquals(anActivity.getUpdatedAt(), actualActivity.getUpdatedAt());
        Assertions.assertEquals(anActivity.getFinishedAt(), actualActivity.getFinishedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());

        final var actualEntity = activityRepository.findById(Long.parseLong(actualActivity.getId().getValue())).get();

        Assertions.assertEquals(expectedCreativeCompanyUserId, actualEntity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualEntity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualEntity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualEntity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedResponse, actualEntity.getResponse());
        Assertions.assertEquals(expectedType, ActivityType.getActivityType((short) actualEntity.getType()));
        Assertions.assertEquals(expectedStatus, ActivityStatus.getActivityStatus((short) actualEntity.getStatus()));
        Assertions.assertEquals(expectedScheduleDate, actualEntity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(anActivity.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(anActivity.getFinishedAt(), actualEntity.getFinishedAt());
        Assertions.assertNull(actualEntity.getFinishedAt());
    }

    @Test
    public void givenAValidActivity_whenCallsUpdate_shoudReturnAActivityUpdate() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDate = DateUtils.convertStringDateToInstant("01/01/2023");

        final var anActivity = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        final var anActivitySave = activityMySQLGateway.create(anActivity);

        Assertions.assertEquals(1, activityRepository.count());

        final var aUpdatedActivity = anActivitySave.clone().update(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedStatus, expectedScheduleDate
        );

        final var actualActivity = activityMySQLGateway.update(aUpdatedActivity);

        Assertions.assertEquals(1, activityRepository.count());

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
        Assertions.assertTrue(anActivity.getUpdatedAt().isBefore(actualActivity.getUpdatedAt()));
        Assertions.assertEquals(anActivity.getFinishedAt(), actualActivity.getFinishedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());

        final var actualEntity = activityRepository.findById(Long.parseLong(actualActivity.getId().getValue())).get();

        Assertions.assertEquals(expectedCreativeCompanyUserId, actualEntity.getCreativeCompanyUserId());
        Assertions.assertEquals(expectedResponsibleCompanyUserId, actualEntity.getResponsibleCompanyUserId());
        Assertions.assertEquals(expectedCompanyId, actualEntity.getCompanyId());
        Assertions.assertEquals(expectedNegotiationId, actualEntity.getNegotiationId());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedResponse, actualEntity.getResponse());
        Assertions.assertEquals(expectedType, ActivityType.getActivityType((short) actualEntity.getType()));
        Assertions.assertEquals(expectedStatus, ActivityStatus.getActivityStatus((short) actualEntity.getStatus()));
        Assertions.assertEquals(expectedScheduleDate, actualEntity.getScheduled());
        Assertions.assertEquals(anActivity.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(anActivity.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(anActivity.getFinishedAt(), actualEntity.getFinishedAt());
        Assertions.assertNull(actualEntity.getFinishedAt());
    }

    @Test
    public void givenAPrePersistedActivityAndValidActivityId_whenCallsFindById_shoudReturnAActivity() throws ParseException {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 1L;
        final var expectedCompanyId = 1L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedScheduleDate = DateUtils.convertStringDateToInstant("01/01/2023");

        final var anActivity = Activity.newActivity(
                expectedCreativeCompanyUserId, expectedResponsibleCompanyUserId, expectedCompanyId,
                expectedNegotiationId, expectedDescription, expectedResponse, expectedType, expectedScheduleDate
        );

        Assertions.assertEquals(0, activityRepository.count());

        final var anActivitySave = activityMySQLGateway.create(anActivity);

        Assertions.assertEquals(1, activityRepository.count());

        final var actualActivity = activityMySQLGateway.findById(anActivitySave.getId()).get();

        Assertions.assertEquals(1, activityRepository.count());

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
        Assertions.assertEquals(anActivity.getUpdatedAt(), actualActivity.getUpdatedAt());
        Assertions.assertEquals(anActivity.getFinishedAt(), actualActivity.getFinishedAt());
        Assertions.assertNull(actualActivity.getFinishedAt());
    }

    @Test
    public void givenValidActivityIdNotStored_whenCallsFindById_shoudReturnEmpty() {
        Assertions.assertEquals(0, activityRepository.count());

        final var actualActivity = activityMySQLGateway.findById(ActivityID.from("1"));

        Assertions.assertEquals(0, activityRepository.count());

        Assertions.assertTrue(actualActivity.isEmpty());
    }

    @Test
    public void givenPrePersistedActivities_whenCallsFindAll_shoudReturnPaginated() throws ParseException {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        final var query = new ActivitySearchQuery(0, 1, "", "creativeCompanyUserId", "asc");
        final var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenEmptyActivitiesTable_whenCallsFindAll_shoudReturnEmptyPage() throws ParseException {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, activityRepository.count());

        final var query = new ActivitySearchQuery(0, 1, "", "creativeCompanyUserId", "asc");
        final var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "", "creativeCompanyUserId", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());

        expectedPage = 1;

        query = new ActivitySearchQuery(1, 1, "", "creativeCompanyUserId", "asc");
        actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity2.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());

        expectedPage = 2;

        query = new ActivitySearchQuery(2, 1, "", "creativeCompanyUserId", "asc");
        actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity3.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityCreativeCompanyUserId_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "creativeCompanyUserId", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityDescription_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "venda", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "ligação", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "compra", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "com", "description", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity3.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityResponsibleCompanyUserId_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "responsibleCompanyUserId", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityCompanyId_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "companyId", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityNegotiationId_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "negotiationId", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityType_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.NOTE,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.EMAIL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "type", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTerms_whenCallsFindAllAndTermsMatchsActivityStatus_shoudReturnPaginated() throws ParseException {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.NOTE,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.EMAIL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "status", "asc");
        var actualResult = activityMySQLGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(anActivity1.getCreativeCompanyUserId(), actualResult.items().get(0).getCreativeCompanyUserId());
    }

    @Test
    public void givenPrePersistedActivitiesAndDocAsTermsInvalid_whenCallsFindAllAndTermsMatchsActivityInvalid_shoudReturnError() throws ParseException {
        final var expectedMessage = "Unable to locate Attribute  with the the given name [tes] on this ManagedType " +
                "[br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity]; nested exception is java.lang.IllegalArgumentException: " +
                "Unable to locate Attribute  with the the given name [tes] on this ManagedType [br.com.powercrm.infrastructure.activity.persistence.ActivityJpaEntity]";

        final var anActivity1 = Activity.newActivity(
                1L, 1L, 1L,
                1L, "teste", "teste", ActivityType.NOTE,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity2 = Activity.newActivity(
                2L, 2L, 2L,
                2L, "teste", "teste", ActivityType.CALL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        final var anActivity3 = Activity.newActivity(
                3L, 3L, 3L,
                3L, "teste", "teste", ActivityType.EMAIL,
                DateUtils.convertStringDateToInstant("12/12/2022")
        );

        Assertions.assertEquals(0, activityRepository.count());

        activityRepository.saveAll(List.of(
                ActivityJpaEntity.from(anActivity1),
                ActivityJpaEntity.from(anActivity2),
                ActivityJpaEntity.from(anActivity3)
        ));

        Assertions.assertEquals(3, activityRepository.count());

        var query = new ActivitySearchQuery(0, 1, "1", "tes", "asc");
        final var actualException =
                Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> activityMySQLGateway.findAll(query));

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

}