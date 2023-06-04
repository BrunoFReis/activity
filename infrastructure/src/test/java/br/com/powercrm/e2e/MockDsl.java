package br.com.powercrm.e2e;

import br.com.powercrm.domain.Identifier;
import br.com.powercrm.domain.activity.ActivityID;
import br.com.powercrm.domain.activity.ActivityType;
import br.com.powercrm.infrastructure.activity.models.ActivityResponse;
import br.com.powercrm.infrastructure.activity.models.CreateActivityRequest;
import br.com.powercrm.infrastructure.activity.models.UpdateActivityRequest;
import br.com.powercrm.infrastructure.configuration.json.Json;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {

    MockMvc mvc();

    /**
     * Category
     */

    default ResultActions deleteAnActivity(final ActivityID anId) throws Exception {
        return this.delete("/activities/cancele/", anId);
    }

    default ResultActions completeAnActivity(final ActivityID anId) throws Exception {
        return this.complete("/activities/complete/", anId);
    }

    default ActivityID givenAnActivity(
            Long creativeCompanyUserId,
            Long responsibleCompanyUserId,
            Long companyId,
            Long negotiationId,
            String description,
            String response,
            ActivityType type,
            String scheduleDate
    ) throws Exception {
        final var aRequestBody = new CreateActivityRequest(
                creativeCompanyUserId,
                responsibleCompanyUserId,
                companyId,
                negotiationId,
                description,
                response,
                type,
                scheduleDate
        );
        final var actualId = this.given("/activities", aRequestBody);
        return ActivityID.from(actualId);
    }

    default ResultActions listActivities(final int page, final int perPage) throws Exception {
        return listActivities(page, perPage, "", "", "");
    }

    default ResultActions listActivities(final int page, final int perPage, final String search) throws Exception {
        return listActivities(page, perPage, search, "", "");
    }

    default ResultActions listActivities(final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        return this.list("/activities", page, perPage, search, sort, direction);
    }

    default ActivityResponse retrieveAnActivity(final ActivityID anId) throws Exception {
        return this.retrieve("/activities/", anId, ActivityResponse.class);
    }

    default ResultActions updateAnActivity(final ActivityID anId, final UpdateActivityRequest aRequest) throws Exception {
        return this.update("/activities/", anId, aRequest);
    }


    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        final var actualId = this.mvc().perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");

        return actualId;
    }

    private ResultActions list(final String url, final int page, final int perPage, final String search, final String sort, final String direction) throws Exception {
        final var aRequest = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8);

        final var json = this.mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions complete(final String url, final Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = MockMvcRequestBuilders.put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return this.mvc().perform(aRequest);
    }
}
