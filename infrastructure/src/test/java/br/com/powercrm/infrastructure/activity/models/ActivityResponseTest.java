package br.com.powercrm.infrastructure.activity.models;

import br.com.powercrm.JacksonTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
public class ActivityResponseTest {

    @Autowired
    private JacksonTester<ActivityResponse> json;

    @Test
    public void testMarshall() throws Exception {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdateAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new ActivityResponse(
                123L,
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                ActivityType.NOTE.getDescription(),
                ActivityStatus.ACTIVE.getDescription(),
                expectedScheduleDateString,
                DateUtils.convertInstantToStringDateTime(expectedCreatedAt),
                DateUtils.convertInstantToStringDateTime(expectedUpdateAt),
                DateUtils.convertInstantToStringDateTime(expectedDeletedAt)
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", 123)
                .hasJsonPathValue("$.creative", expectedCreativeCompanyUserId)
                .hasJsonPathValue("$.responsible", expectedResponsibleCompanyUserId)
                .hasJsonPathValue("$.company", expectedCompanyId)
                .hasJsonPathValue("$.negotiation", expectedNegotiationId)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.response", expectedResponse)
                .hasJsonPathValue("$.type", ActivityType.NOTE.getDescription())
                .hasJsonPathValue("$.status", ActivityStatus.ACTIVE.getDescription())
                .hasJsonPathValue("$.scheduleDate", expectedScheduleDateString)
                .hasJsonPathValue("$.created_at", DateUtils.convertInstantToStringDateTime(expectedCreatedAt))
                .hasJsonPathValue("$.updated_at", DateUtils.convertInstantToStringDateTime(expectedUpdateAt))
                .hasJsonPathValue("$.finish_at", DateUtils.convertInstantToStringDateTime(expectedDeletedAt));
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdateAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                  "id": "%s",
                  "creative": %s,
                  "responsible": "%s",
                  "company": %s,
                  "negotiation": "%s",
                  "description": "%s",
                  "response": "%s",
                  "type": "%s",
                  "status": "%s",
                  "scheduleDate": "%s",
                  "created_at": "%s",
                  "updated_at": "%s",
                  "finish_at": "%s"
                }    
                """.formatted(
                123L,
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                ActivityType.NOTE.getDescription(),
                ActivityStatus.ACTIVE.getDescription(),
                expectedScheduleDateString,
                DateUtils.convertInstantToStringDateTime(expectedCreatedAt),
                DateUtils.convertInstantToStringDateTime(expectedUpdateAt),
                DateUtils.convertInstantToStringDateTime(expectedDeletedAt)

        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", 123L)
                .hasFieldOrPropertyWithValue("creativeCompanyUserId", expectedCreativeCompanyUserId)
                .hasFieldOrPropertyWithValue("responsibleCompanyUserId", expectedResponsibleCompanyUserId)
                .hasFieldOrPropertyWithValue("companyId", expectedCompanyId)
                .hasFieldOrPropertyWithValue("negotiationId", expectedNegotiationId)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("response", expectedResponse)
                .hasFieldOrPropertyWithValue("type", ActivityType.NOTE.getDescription())
                .hasFieldOrPropertyWithValue("status", ActivityStatus.ACTIVE.getDescription())
                .hasFieldOrPropertyWithValue("scheduleDate", expectedScheduleDateString)
                .hasFieldOrPropertyWithValue("createdAt", DateUtils.convertInstantToStringDateTime(expectedCreatedAt))
                .hasFieldOrPropertyWithValue("updatedAt", DateUtils.convertInstantToStringDateTime(expectedUpdateAt))
                .hasFieldOrPropertyWithValue("finishAt", DateUtils.convertInstantToStringDateTime(expectedDeletedAt));
    }
}
