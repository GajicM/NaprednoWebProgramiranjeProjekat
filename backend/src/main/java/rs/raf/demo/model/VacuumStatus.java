package rs.raf.demo.model;

public enum VacuumStatus {
    ON("ON"),
    OFF("OFF"),
    DISCHARGING("DISCHARGING");

    private final String value;

    VacuumStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
