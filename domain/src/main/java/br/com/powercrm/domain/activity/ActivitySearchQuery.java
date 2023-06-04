package br.com.powercrm.domain.activity;

public record ActivitySearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
