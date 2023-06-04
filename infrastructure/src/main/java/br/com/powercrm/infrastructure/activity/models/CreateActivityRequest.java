package br.com.powercrm.infrastructure.activity.models;

import br.com.powercrm.domain.activity.ActivityType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateActivityRequest(
        @JsonProperty("creative") Long creativeCompanyUserId,
        @JsonProperty("responsible") Long responsibleCompanyUserId,
        @JsonProperty("company") Long companyId,
        @JsonProperty("negotiation") Long negotiationId,
        @JsonProperty("description") String description,
        @JsonProperty("response") String response,
        @JsonProperty("type") ActivityType type,
        @JsonProperty("scheduleDate") String scheduleDate
) {
}
