package br.com.powercrm.domain.activity;

import br.com.powercrm.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class ActivityID extends Identifier {

    private final String value;

    private ActivityID(String value) {
        Objects.requireNonNull(value, "'id' should not be null");
        this.value = value;
    }

    public static ActivityID unique() {
        return ActivityID.from(UUID.randomUUID());
    }

    public static ActivityID from(final String anId) {
        return new ActivityID(anId);
    }

    public static ActivityID from(final UUID anId) {
        return new ActivityID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ActivityID that = (ActivityID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}