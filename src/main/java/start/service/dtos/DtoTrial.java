package start.service.dtos;

import start.domain.Participant;
import start.domain.Trial;

import java.util.List;

public class DtoTrial {

    private Trial trial;
    private int nrOfEnrollments;

    public DtoTrial(Trial trial, int nrOfEnrollments) {
        this.trial = trial;
        this.nrOfEnrollments = nrOfEnrollments;
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
