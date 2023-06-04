package br.com.powercrm.infrastructure.activity.models;

import br.com.powercrm.JacksonTest;
import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateActivityRequestTest {

    @Autowired
    private JacksonTester<UpdateActivityRequest> json;

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedCreativeCompanyUserId = 1L;
        final var expectedResponsibleCompanyUserId = 2L;
        final var expectedCompanyId = 2L;
        final var expectedNegotiationId = 1L;
        final var expectedDescription = "atividade";
        final var expectedResponse = "atividade";
        final var expectedScheduleDateString = "01/01/2023";

        final var json = """
                {
                  "creative": %s,
                  "responsible": "%s",
                  "company": %s,
                  "negotiation": "%s",
                  "description": "%s",
                  "response": "%s",
                  "type": "%s",
                  "status": "%s",
                  "scheduleDate": "%s"                  
                }    
                """.formatted(
                expectedCreativeCompanyUserId,
                expectedResponsibleCompanyUserId,
                expectedCompanyId,
                expectedNegotiationId,
                expectedDescription,
                expectedResponse,
                ActivityType.NOTE,
                ActivityStatus.ACTIVE,
                expectedScheduleDateString
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("creativeCompanyUserId", expectedCreativeCompanyUserId)
                .hasFieldOrPropertyWithValue("responsibleCompanyUserId", expectedResponsibleCompanyUserId)
                .hasFieldOrPropertyWithValue("companyId", expectedCompanyId)
                .hasFieldOrPropertyWithValue("negotiationId", expectedNegotiationId)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("response", expectedResponse)
                .hasFieldOrPropertyWithValue("type", ActivityType.NOTE)
                .hasFieldOrPropertyWithValue("status", ActivityStatus.ACTIVE)
                .hasFieldOrPropertyWithValue("scheduleDate", expectedScheduleDateString);
    }
}
