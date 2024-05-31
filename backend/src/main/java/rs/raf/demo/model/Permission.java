package rs.raf.demo.model;

public enum Permission {
    can_read_users("can_read_users"),
    can_create_users("can_create_users"),
    can_update_users("can_update_users"),
    can_delete_users("can_delete_users"),
    can_search_vacuum("can_search_vacuum"),
    can_start_vacuum("can_start_vacuum"),
    can_stop_vacuum("can_stop_vacuum"),
    can_discharge_vacuum("can_discharge_vacuum"),
    can_add_vacuum("can_add_vacuum"),
    can_remove_vacuum("can_remove_vacuum"),

    ;


    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
