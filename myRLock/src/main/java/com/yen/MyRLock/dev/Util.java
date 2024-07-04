package com.yen.MyRLock.dev;

public class Util {

    public Integer add(int n1, int n2){
        return n1 + n2;
    }

    public Integer substract(int n1, int n2){
        return n1 - n2;
    }

    public Integer product(int n1, int n2){
        return n1 * n2;
    }

    // division
    public Integer division(int n1, int n2){
        if (n2 == 0){
            throw new ArithmeticException("n2 can not be zero");
        }
        return n1 / n2;
    }

    public Integer square(int n1){
        return n1 * n1;
    }

    public Double sin(int n1){
        return Math.sin(n1);
    }

}