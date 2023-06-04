package br.com.powercrm.domain.activity;

public enum ActivityStatus {

    ACTIVE(1, "Ativada"),
    COMPLETED(2, "Completada"),
    CANCELED(3, "Cancelada");

    private int id;

    private String description;

    ActivityStatus(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static ActivityStatus getActivityStatus(Short id) {
        if (id != null) {
            switch (id) {
                case 1:
                    return ACTIVE;
                case 2:
                    return COMPLETED;
                case 3:
                    return CANCELED;
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
