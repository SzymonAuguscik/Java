package Maths;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @org.junit.jupiter.api.Test
    void testToString() {
        Polynomial pol = new Polynomial(new double[]{1,2,-1,0,-1});
        assertEquals(pol.toString(),"-x^4-x^2+2x+1");
    }

    @org.junit.jupiter.api.Test
    void add() {
        Polynomial pol1 = new Polynomial(new double[]{2,-3,4,-2});
        Polynomial pol2 = new Polynomial(new double[]{3,3,2,-1});
        Polynomial pol3 = pol1.add(pol2);
        assertEquals(pol3.toString(),"-3x^3+6x^2+5");
    }

    @org.junit.jupiter.api.Test
    void sub() {
        Polynomial pol1 = new Polynomial(new double[]{5,-2,-3,0,9});
        Polynomial pol2 = new Polynomial(new double[]{3,-2,-2,5});
        Polynomial pol3 = pol1.sub(pol2);
        assertEquals(pol3.toString(),"9x^4-5x^3-x^2+2");
    }

    @org.junit.jupiter.api.Test
    void mul() {
        Polynomial pol1 = new Polynomial(new double[]{1,2});
        Polynomial pol2 = new Polynomial(new double[]{-3,4,-1});
        Polynomial pol3 = pol1.mul(pol2);
        assertEquals(pol3.toString(),"-2x^3+7x^2-2x-3");
    }

    @org.junit.jupiter.api.Test
    void div() {
        Polynomial pol1 = new Polynomial(new double[]{0,6,45,21,12,6});
        Polynomial pol2 = new Polynomial(new double[]{0,6,3});
        Polynomial pol3 = pol1.div(pol2);
        assertEquals(pol3.toString(),"2x^3+7x+1");
    }

    @org.junit.jupiter.api.Test
    void removeLastZerosList() {
        List<Double> list1 = new ArrayList<>();
        List<Double> list2 = new ArrayList<>();

        int CONST = 5;

        for (int i = 0; i < CONST; i++) {
            list1.add((double) i);
            list2.add((double) i);
        }
        for (int i = 0; i < CONST; i++)
            list1.add((double) 0);

        Polynomial pol = new Polynomial(new double[]{0});
        pol.removeLastZeros(list1);

        assertEquals(list1, list2);
    }

    @org.junit.jupiter.api.Test
    void removeLastZerosPolynomial() {
        Polynomial pol = new Polynomial(new double[] {1,1,2,0,0,0});
        pol.removeLastZeros(pol);

        assertEquals(pol.toString(),"2x^2+x+1");
    }

    @org.junit.jupiter.api.Test
    void evaluate() {
        Polynomial pol = new Polynomial(new double[]{1,2,-5,2});
        assertEquals(pol.evaluate(2),1);
    }

    @org.junit.jupiter.api.Test
    void derivative() {
        Polynomial pol = new Polynomial(new double[]{1,2,5,0,5});
        assertEquals(pol.derivative().toString(),"20x^3+10x+2");
    }
}