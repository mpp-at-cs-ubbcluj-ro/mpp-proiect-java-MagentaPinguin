package start.domain;

public class Office extends Entity<Long> {

    private String username;
    private String password;

    public Office() {
    }

    public Office(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Office(Long aLong, String username, String password) {
        super(aLong);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Office office = (Office) o;

        if (getUsername() != null ? !getUsername().equals(office.getUsername()) : office.getUsername() != null)
            return false;
        return getPassword() != null ? getPassword().equals(office.getPassword()) : office.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getUsername() != null ? getUsername().hashCode() : 0;
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Office: username=" + username + " password=" + password;
    }
}
