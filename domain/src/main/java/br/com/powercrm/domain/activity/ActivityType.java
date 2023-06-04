package br.com.powercrm.domain.activity;

public enum ActivityType {

    NOTE(1, "Note"),
    CALL(2, "Call"),
    WHATSAPP(3, "whatsapp"),
    EMAIL(4, "Email"),
    VISIT(5, "Visit"),
    CLOSING_FORECAST(6, "Closing Forecast");

    private int id;

    private String description;

    ActivityType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static ActivityType getActivityType(Short id) {
        if (id != null) {
            switch (id) {
                case 1:
                    return NOTE;
                case 2:
                    return CALL;
                case 3:
                    return WHATSAPP;
                case 4:
                    return EMAIL;
                case 5:
                    return VISIT;
                case 6:
                    return CLOSING_FORECAST;
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
