package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;

@Data
public class SubjectStatistics {
    private Subject subject;
    private Double expenses;
    private Double profits;
    private Double sum;
    private Double count;

    public SubjectStatistics(){


    }

    public SubjectStatistics(Subject subject, Double expenses, Double profits, Double sum,Double count){
        this.subject = subject;
        this.expenses = expenses;
        this.profits = profits;
        this.sum = sum;
        this.count = count;
    };
    public SubjectStatistics(Subject subject, Double expenses, Double profits, Double count){
        this.subject = subject;
        this.expenses = expenses;
        this.profits = profits;
        this.sum = profits-expenses;
        this.count = count;
    };
}
