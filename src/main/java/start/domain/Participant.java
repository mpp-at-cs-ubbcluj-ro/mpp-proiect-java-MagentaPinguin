package start.domain;

public class Participant extends Entity<Long> {

    private String name;
    private String cnp;
    private int age;

    public Participant() {
    }

    public Participant(String name, String cnp, int age) {
        this.name = name;
        this.cnp = cnp;
        this.age = age;
    }

    public Participant(Long aLong, String name, String cnp, int age) {
        super(aLong);
        this.name = name;
        this.cnp = cnp;
        this.age = age;
    }

    public String getName() {
        return name;
    }


    public String getCnp() {
        return cnp;
    }


    public int getAge() {
        return age;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Participant that = (Participant) o;

        if (getAge() != that.getAge()) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getCnp() != null ? getCnp().equals(that.getCnp()) : that.getCnp() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getCnp() != null ? getCnp().hashCode() : 0);
        result = 31 * result + getAge();
        return result;
    }

    @Override
    public String toString() {
        return "Participant{" +
                "name='" + name + '\'' +
                ", cnp='" + cnp + '\'' +
                ", age=" + age +
                '}';
    }
}
