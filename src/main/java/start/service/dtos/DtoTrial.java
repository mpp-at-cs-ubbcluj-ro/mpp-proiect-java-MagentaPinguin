package start.service.dtos;


import start.domain.Trial;



public class DtoTrial {

    private final Trial trial;
    private final int nrOfEnrollments;

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




}
