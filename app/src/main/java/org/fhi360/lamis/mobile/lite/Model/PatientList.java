package org.fhi360.lamis.mobile.lite.Model;

import org.fhi360.lamis.mobile.lite.Model.Patient;

import java.util.List;

public class PatientList {
    List<Patient> patient;

    public void setPatient(List<Patient> patient) {
        this.patient = patient;
    }

    public List<Patient> getPatient() {
        return patient;
    }
}
