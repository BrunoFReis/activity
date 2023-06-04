package br.com.powercrm.infrastructure.activity.models;

import br.com.powercrm.domain.activity.ActivityStatus;
import br.com.powercrm.domain.activity.ActivityType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateActivityRequest(
        @JsonProperty("creative") Long creativeCompanyUserId,
        @JsonProperty("responsible") Long responsibleCompanyUserId,
        @JsonProperty("company") Long companyId,
        @JsonProperty("negotiation") Long negotiationId,
        @JsonProperty("description") String description,
        @JsonProperty("response") String response,
        @JsonProperty("type") ActivityType type,
        @JsonProperty("status") ActivityStatus status,
        @JsonProperty("scheduleDate") String scheduleDate
) {
}
