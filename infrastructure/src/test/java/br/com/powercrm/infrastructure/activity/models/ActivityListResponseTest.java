package br.com.powercrm.infrastructure.activity.models;

import br.com.powercrm.JacksonTest;
import br.com.powercrm.application.utils.DateUtils;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;

@JacksonTest
class ActivityListResponseTest {

    @Autowired
    private JacksonTester<ActivityListResponse> json;

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

        final var response = new ActivityListResponse(
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
                .hasJsonPathValue("$.createdAt", DateUtils.convertInstantToStringDateTime(expectedCreatedAt))
                .hasJsonPathValue("$.updatedAt", DateUtils.convertInstantToStringDateTime(expectedUpdateAt))
                .hasJsonPathValue("$.finishAt", DateUtils.convertInstantToStringDateTime(expectedDeletedAt));
    }
}