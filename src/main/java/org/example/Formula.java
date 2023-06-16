package org.example;



public class Formula {


    private double volume;
    private String sex;
    private int weight;

    private double result;

    private double drink;

    public Formula() {

    }


    public double getResult() {
        return result;
    }
    public void setResult(double result) {
        this.result = result;
    }






    public Formula(String sex, int weight, double drink, double volume,double result) {
        this.sex = sex;
        this.weight = weight;
        this.drink = drink;
        this.volume = volume;
        this.result =result;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }

     public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public double getDrink() {
        return drink;
    }
    public void setDrink(double drink) {
        this.drink = drink;
    }



}
