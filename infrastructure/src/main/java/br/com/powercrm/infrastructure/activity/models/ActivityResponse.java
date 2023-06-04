package br.com.powercrm.infrastructure.activity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ActivityResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("creative") Long creativeCompanyUserId,
        @JsonProperty("responsible") Long responsibleCompanyUserId,
        @JsonProperty("company") Long companyId,
        @JsonProperty("negotiation") Long negotiationId,
        @JsonProperty("description") String description,
        @JsonProperty("response") String response,
        @JsonProperty("type") String type,
        @JsonProperty("status") String status,
        @JsonProperty("scheduleDate") String scheduleDate,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("updated_at") String updatedAt,
        @JsonProperty("finish_at") String finishAt
) {
}
