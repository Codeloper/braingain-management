package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;

@Data
public class Statistic {
    private Tutor tutor;
    private Double expenses;
    private Double profits;
    private Double sum;
    private Double count;

    public Statistic(){


    }

    public Statistic(Tutor tutor, Double expenses, Double profits, Double sum, Double count){
        this.tutor = tutor;
        this.expenses = expenses;
        this.profits = profits;
        this.sum = sum;
        this.count = count;
    };
    public Statistic(Tutor tutor, Double expenses, Double profits, Double count){
        this.tutor = tutor;
        this.expenses = expenses;
        this.profits = profits;
        this.sum = profits-expenses;
        this.count = count;
    };


}
