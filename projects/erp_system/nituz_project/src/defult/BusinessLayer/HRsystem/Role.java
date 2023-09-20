package defult.BusinessLayer.HRsystem;

public enum Role {
    SHIFT_MANAGER("Shift Manager"),
    CASHIER("Cashier"),
    STORAGE("Storage"),
    SECURITY("Security"),
    CLEANER("Cleaner"),
    ORGANIZER("Organizer"),
    DRIVER("Driver"),
    GENERAL("General");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
