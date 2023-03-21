package start.domain;

import java.util.Objects;

public class Enrolled extends Entity<Long> {
    private Participant participant;
    private Trial trial;

    public Enrolled() {
    }

    public Enrolled(Participant participant, Trial trial) {
        this.participant = participant;
        this.trial = trial;
    }

    public Enrolled(Long aLong, Participant participant, Trial trial) {
        super(aLong);
        this.participant = participant;
        this.trial = trial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Enrolled enrolled = (Enrolled) o;

        if (!Objects.equals(participant, enrolled.participant))
            return false;
        return Objects.equals(trial, enrolled.trial);
    }

    @Override
    public int hashCode() {
        int result = participant != null ? participant.hashCode() : 0;
        result = 31 * result + (trial != null ? trial.hashCode() : 0);
        return result;
    }
}
