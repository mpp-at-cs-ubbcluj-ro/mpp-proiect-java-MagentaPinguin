package start.domain;

public class Entity<ID> {
    private ID id;
    public Entity() {
    }
    public Entity(ID id) {
        this.id = id;
    }
    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }
}
