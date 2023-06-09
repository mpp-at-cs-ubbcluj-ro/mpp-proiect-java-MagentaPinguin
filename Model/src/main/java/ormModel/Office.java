package ormModel;

import javax.persistence.*;

@Entity
@Table(name="offices")
public class Office  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_office")
    private  long id;
    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    public Office() {
    }

    public Office(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Office(Long aLong, String username, String password) {
        this.id=aLong;
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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
