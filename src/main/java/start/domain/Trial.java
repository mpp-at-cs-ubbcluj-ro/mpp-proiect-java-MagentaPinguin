package start.domain;

public class Trial extends Entity<Long> {
    private String name;
    private int minAge;
    private int maxAge;

    public Trial() {
    }

    public Trial(String name, int minAge, int maxAge) {
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public Trial(Long aLong, String name, int minAge, int maxAge) {
        super(aLong);
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trial trial = (Trial) o;

        if (getMinAge() != trial.getMinAge()) return false;
        if (getMaxAge() != trial.getMaxAge()) return false;
        return getName() != null ? getName().equals(trial.getName()) : trial.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + getMinAge();
        result = 31 * result + getMaxAge();
        return result;
    }

    @Override
    public String toString() {
        return "Trial{" +
                "name='" + name + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                '}';
    }
}
