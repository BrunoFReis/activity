package br.com.powercrm.e2e.activity;

import br.com.powercrm.E2ETest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.e2e.MockDsl;
import br.com.powercrm.infrastructure.activity.models.UpdateActivityRequest;
import br.com.powercrm.infrastructure.activity.persistence.ActivityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class ActivityE2ETest implements MockDsl {

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("powercrm_activity");


    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MY_SQL_CONTAINER.getMappedPort(3306));
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewActivityWithValidValues() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedType = ActivityType.NOTE;
        final var expectedTypeInt = ActivityType.NOTE.getId();
        final var expectedStatusInt = ActivityStatus.ACTIVE.getId();

        final var actualId = givenAnActivity(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        final var actualActivity = activityRepository.findById(Long.parseLong(actualId.getValue())).get();

        Assertions.assertNotNull(actualActivity);
        Assertions.assertNotNull(actualActivity.getId());
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
    public void asACatalogAdminIShouldBeAbleToNavigateToAllActivities() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "email",
                "email",
                ActivityType.EMAIL,
                "01/01/2023"
        );

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "wats",
                "wats",
                ActivityType.WHATSAPP,
                "01/01/2023"
        );

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "telefone",
                "telefone",
                ActivityType.CALL,
                "01/01/2023"
        );

        listActivities(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].description", equalTo("email")));

        listActivities(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].description", equalTo("telefone")));

        listActivities(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].description", equalTo("wats")));

        listActivities(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllActivities() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "email",
                "email",
                ActivityType.EMAIL,
                "01/01/2023"
        );

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "wats",
                "wats",
                ActivityType.WHATSAPP,
                "01/01/2023"
        );

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "telefone",
                "telefone",
                ActivityType.CALL,
                "01/01/2023"
        );

        listActivities(0, 1, "wa")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].description", equalTo("wats")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllActivitiesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "email",
                "email",
                ActivityType.EMAIL,
                "01/01/2023"
        );

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "wats",
                "wats",
                ActivityType.WHATSAPP,
                "01/01/2023"
        );

        givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "telefone",
                "telefone",
                ActivityType.CALL,
                "01/01/2023"
        );

        listActivities(0, 3, "", "response", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].description", equalTo("wats")))
                .andExpect(jsonPath("$.items[1].description", equalTo("telefone")))
                .andExpect(jsonPath("$.items[2].description", equalTo("email")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAnActivityByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedType = ActivityType.NOTE;
        final var expectedTypeString = ActivityType.NOTE.getDescription();
        final var expectedStatusString = ActivityStatus.ACTIVE.getDescription();

        final var actualId = givenAnActivity(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                expectedType,
                expectedScheduleDateString
        );

        final var actualActivity = retrieveAnActivity(actualId);

        Assertions.assertNotNull(actualActivity);
        Assertions.assertNotNull(actualActivity.id());
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
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundActivity() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        final var aRequest = get("/activities/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Activity with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAnActivityByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        final var actualId = givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "email",
                "email",
                ActivityType.EMAIL,
                "10/01/2023"
        );

        final var expectedCreativeCompanyUserId = 2L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 2L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedType = ActivityType.NOTE;
        final var expectedStatus = ActivityStatus.ACTIVE;
        final var expectedTypeInt = ActivityType.NOTE.getId();
        final var expectedStatusInt = ActivityStatus.ACTIVE.getId();

        final var aRequestBody = new UpdateActivityRequest(
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

        updateAnActivity(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualActivity = activityRepository.findById(Long.parseLong(actualId.getValue())).get();

        Assertions.assertNotNull(actualActivity);
        Assertions.assertNotNull(actualActivity.getId());
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
    public void asACatalogAdminIShouldBeAbleToCanceledAnActivityByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        final var actualId = givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "email",
                "email",
                ActivityType.EMAIL,
                "10/01/2023"
        );

        Assertions.assertEquals(1, activityRepository.count());

        final var oldActivity = activityRepository.findById(Long.parseLong(actualId.getValue())).get();

        deleteAnActivity(actualId)
                .andExpect(status().isNoContent());

        final var actualActivity = activityRepository.findById(Long.parseLong(actualId.getValue())).get();

        Assertions.assertEquals(1, activityRepository.count());


        Assertions.assertEquals(ActivityStatus.CANCELED.getId(), actualActivity.getStatus());
        Assertions.assertTrue(oldActivity.getUpdatedAt().isBefore(actualActivity.getUpdatedAt()));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCompletedAnActivityByItsIdentifier() throws Exception {
        Assertions.assertTrue(MY_SQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, activityRepository.count());

        final var actualId = givenAnActivity(
                1L,
                1L,
                1L,
                1L,
                "email",
                "email",
                ActivityType.EMAIL,
                "10/01/2023"
        );

        Assertions.assertEquals(1, activityRepository.count());

        final var oldActivity = activityRepository.findById(Long.parseLong(actualId.getValue())).get();

        Assertions.assertNull(oldActivity.getFinishedAt());

        completeAnActivity(actualId)
                .andExpect(status().isNoContent());

        final var actualActivity = activityRepository.findById(Long.parseLong(actualId.getValue())).get();

        Assertions.assertEquals(1, activityRepository.count());


        Assertions.assertEquals(ActivityStatus.COMPLETED.getId(), actualActivity.getStatus());
        Assertions.assertTrue(oldActivity.getUpdatedAt().isBefore(actualActivity.getUpdatedAt()));
        Assertions.assertNotNull(actualActivity.getFinishedAt());
    }
}
