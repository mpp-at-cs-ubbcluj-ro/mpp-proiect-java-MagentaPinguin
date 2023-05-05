package model;

import java.io.Serializable;

public class DtoTrial implements Serializable {

    private Trial trial;
    private int nrOfEnrollments;

    public DtoTrial(Trial trial, int nrOfEnrollments) {
        this.trial = trial;
        this.nrOfEnrollments = nrOfEnrollments;
    }

    public long getId() {
        return trial.getId();
    }
    public Trial getTrial() {
        return trial;
    }

    public int getNrOfEnrollments() {
        return nrOfEnrollments;
    }

    public String getName() {
        return trial.getName();
    }

    public int getMinAge() {
        return trial.getMinAge();
    }

    public int getMaxAge() {
        return trial.getMaxAge();
    }


}
