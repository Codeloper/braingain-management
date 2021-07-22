package de.unisiegen.propra.groupfour.braingainmanagement.data.entity;

import lombok.Data;

@Data
public class Statistic {
    private Tutor tutor;
    private Double expenses;
    private Double profits;
    private Double sum;

    public Statistic(){


    }

    public Statistic(Tutor tutor, Double expenses, Double profits, Double sum){
        this.tutor = tutor;
        this.expenses = expenses;
        this.profits = profits;
        this.sum = sum;
    };
    public Statistic(Tutor tutor, Double expenses, Double profits){
        this.tutor = tutor;
        this.expenses = expenses;
        this.profits = profits;
        this.sum = profits-expenses;
    };


}
