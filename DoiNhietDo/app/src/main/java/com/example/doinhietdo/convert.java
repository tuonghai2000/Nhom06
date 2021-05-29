package com.example.doinhietdo;

public class convert {
    public double c, f;

    public convert() {

    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }
    public void convertCtoF() {
        this.f = this.c * 9 / 5 + 32;
    }

    public void convertFtoC() {
        this.c = (this.f - 32) * 5 / 9;
    }
}
