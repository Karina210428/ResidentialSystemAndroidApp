package model;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;

public class Request implements Serializable {

    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    private Integer id;

    @SerializedName("problem")
    @Expose
    private String problem;

    @SerializedName("isDone")
    @Expose
    private Boolean isDone;

    @SerializedName("dateOfRequest")
    @Expose
    private LocalDate dateOfRequest;

    @SerializedName("dateOfDeadline")
    @Expose
    private LocalDate dateOfDeadline;

    @SerializedName("confirm")
    @Expose
    private Boolean confirm;

    @Nullable
    @SerializedName("occupant")
    @Expose
    private Occupant occupant;

    @Nullable
    @SerializedName("employee")
    @Expose
    private Employee employee;

    @SerializedName("specialization")
    @Expose
    private Specialization specialization;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public LocalDate getDateOfRequest() {
        return dateOfRequest;
    }

    public void setDateOfRequest(LocalDate dateOfRequest) {
        this.dateOfRequest = dateOfRequest;
    }

    public LocalDate getDateOfDeadline() {
        return dateOfDeadline;
    }

    public void setDateOfDeadline(LocalDate dateOfDeadline) {
        this.dateOfDeadline = dateOfDeadline;
    }

    public Occupant getOccupant() {
        return occupant;
    }

    public void setOccupant(Occupant occupant) {
        this.occupant = occupant;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public void setConfirm(Boolean confirm) {
        this.confirm = confirm;
    }
}
