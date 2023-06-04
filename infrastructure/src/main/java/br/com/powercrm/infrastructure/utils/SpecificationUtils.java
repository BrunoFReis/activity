package br.com.powercrm.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get(prop)), like(term.toUpperCase()));
    }

    public static <T> Specification<T> equalsLong(final String prop, final String term) {
        return (root, query, cb) ->
                cb.equal(cb.upper(root.get(prop)), parseLong(term));
    }

    private static String like(final String term) {
        return "%" + term + "%";
    }

    private static Long parseLong(String str) {
        try
        {
            return Long.parseLong(str);
        } catch (NumberFormatException ex)
        {
            return 0L;
        }
    }
}
