package eshatikhin.project.entities;

import eshatikhin.project.entities.enums.CheckStatus;

import java.sql.Timestamp;
import java.util.Objects;

public class Check {
    private int id;
    private Timestamp timestamp;
    private CheckStatus status;
    private double cost;
    private int cashier_id;

    public Check() {
    }

    public Check(Timestamp timestamp, CheckStatus status, double cost, int cashier_id) {
        this.timestamp = timestamp;
        this.status = status;
        this.cost = cost;
        this.cashier_id = cashier_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public CheckStatus getStatus() {
        return status;
    }

    public void setStatus(CheckStatus status) {
        this.status = status;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Check check = (Check) o;
        return getId() == check.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
