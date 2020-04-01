package Maths;

import java.text.DecimalFormat;
import java.util.*;

public class Polynomial {
    public int degree;
    private static class Monomial {
        int degree;
        double coefficient;

        Monomial(int d, double c) {
            this.degree = d;
            this.coefficient = c;
        }
    }

    List<Monomial> monomialList = new ArrayList<>();

    Polynomial (double[] args) {
        for (int i = 0; i < args.length; i++) {
            Monomial m = new Monomial(i, args[i]);
            this.monomialList.add(m);
        }

        removeLastZeros(this);
        this.degree = this.monomialList.size()-1;
    }

    Polynomial (List<Double> args) {
        for (int i = 0; i < args.size(); i++) {
            Monomial m = new Monomial(i,args.get(i));
            this.monomialList.add(m);
        }

        removeLastZeros(this);
        this.degree = monomialList.size()-1;
    }

    Polynomial (Polynomial pol) {
        for (var mono : pol.monomialList) {
            Monomial m = new Monomial(mono.degree,mono.coefficient);
            this.monomialList.add(m);
        }

        removeLastZeros(this);
        this.degree = this.monomialList.size()-1;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        DecimalFormat df = new DecimalFormat("0.#");

        if (this.monomialList.get(0).coefficient == 0 && this.degree == 0)
            s.append(0);

        else {
            for (int i = this.monomialList.size()-1; i >= 0; i--) {
                Monomial mono = monomialList.get(i);
                if (mono.coefficient != 0) {
                    if(mono.coefficient > 0 && i != this.degree) {                                                         //don't print "+" in the beginning
                        s.append("+");
                    }
                    if ((Math.abs(mono.coefficient) == 1 && mono.degree != 0)) {                                           //don't print "+/-1" before powers of x
                        if (mono.coefficient == -1)
                            s.append("-");
                    }
                    else
                        s.append(df.format(mono.coefficient));

                    if (mono.degree > 1) {                                                                                  //for powers greater than 1 print "^"
                        s.append("x^");
                        s.append(mono.degree);
                    }
                    else if (mono.degree == 1)                                                                              //no need to print "^" for 1st power
                        if (mono.coefficient == -1) {                                                                       //no need to print "+/-1" before 1st power
                            s.append("-x");
                        } else {
                            s.append("x");
                        }
                }
            }
        }
        return s.toString();
    }

    public Polynomial add(Polynomial addPoly) {
        Polynomial maxPoly = this.degree > addPoly.degree?this:addPoly;
        int maxDegree = maxPoly.degree;
        int minDegree = Math.min(this.degree, addPoly.degree);
        List<Double> coefficientList = new ArrayList<>();

        for (int i = 0; i <= minDegree; i++)
            coefficientList.add(this.monomialList.get(i).coefficient + addPoly.monomialList.get(i).coefficient);
        for (int i = 1; i <= maxDegree - minDegree; i++)
            coefficientList.add(maxPoly.monomialList.get(i+minDegree).coefficient);

        return new Polynomial(coefficientList);
    }

    public Polynomial sub(Polynomial subPoly) {
        List<Double> degreeList = new ArrayList<>();
        for(var mono : subPoly.monomialList)
            degreeList.add(-mono.coefficient);
        return add(new Polynomial(degreeList));
    }

    public Polynomial mul(Polynomial mulPoly) {
        HashMap<Integer,Double> coefficientMap = new HashMap<>();
        List<Double> coefficientList = new ArrayList<>();

        for (int i = 0; i < this.monomialList.size(); i++) {
            for (int j = 0; j < mulPoly.monomialList.size(); j++) {
                int currentDegree = this.monomialList.get(i).degree + mulPoly.monomialList.get(j).degree;
                double value = this.monomialList.get(i).coefficient * mulPoly.monomialList.get(j).coefficient;

                if (coefficientMap.containsKey(currentDegree))
                    value += coefficientMap.get(currentDegree);

                coefficientMap.put(currentDegree,value);
            }
        }

        for(var i : coefficientMap.keySet())
            coefficientList.add(coefficientMap.get(i));

        return new Polynomial(coefficientList);
    }

    public Polynomial div(Polynomial divPoly) {
        List<Double> restList = new ArrayList<>();
        List<Double> coefficientList = new ArrayList<>();
        double maxDivCoefficient = divPoly.monomialList.get(divPoly.monomialList.size()-1).coefficient;
        double currentCoefficient;
        int currentDegree = this.degree - divPoly.degree;

        for (var mono : this.monomialList)
            restList.add(mono.coefficient);

        if (currentDegree >= 0) {

            for (int i = 0; i <= currentDegree; i++)
                coefficientList.add(0.0);

            for (;currentDegree >= 0;) {
                currentCoefficient = restList.get(restList.size() - 1)/maxDivCoefficient;
                for (int i = 0; i < divPoly.monomialList.size(); i++)
                    restList.set(restList.size()-1 - i, restList.get(restList.size()-1 - i) - currentCoefficient*divPoly.monomialList.get(divPoly.monomialList.size()-1 - i).coefficient);
                removeLastZeros(restList);
                coefficientList.set(currentDegree,currentCoefficient);
                currentDegree = restList.size() - 1 - divPoly.degree;
            }

            if (restList.size() == 1 && restList.get(0) == 0) {
                return new Polynomial(coefficientList);
            }

            System.out.println("Cannot divide polynomial! It is not divisible!");
            return this;
        }

        System.out.println("Cannot divide polynomial! Its degree is too low!");
        return this;
    }

    public void removeLastZeros(List<Double> list) {
        int i = list.size()-1;
        if (i > 0 && list.get(i) == 0) {
            list.remove(i);
            removeLastZeros(list);
        }
    }

    public void removeLastZeros(Polynomial pol) {
        for (int i = pol.monomialList.size()-1; i > 0; i--)
            if (pol.monomialList.get(i).coefficient == 0)
                pol.monomialList.remove(i);
            else
                break;
    }

    public double evaluate(double value) {
        double result = 0;

        for (var mono : monomialList)
            result += mono.coefficient * Math.pow(value,mono.degree);

        return result;
    }

    public Polynomial derivative() {
        Polynomial pol = new Polynomial(this);

        if (this.degree == 0)
            return new Polynomial(new double[]{0});

        pol.monomialList.remove(0);

        for (var mono : pol.monomialList) {
            mono.coefficient = mono.coefficient*mono.degree;
            mono.degree -= 1;
        }

        pol.degree = pol.monomialList.size()-1;

        return pol;
    }

    public static void main (String[] args) {
        Polynomial pol1 = new Polynomial(new double[]{-1,0,2});
        Polynomial pol2 = new Polynomial(new double[]{3,0,4,0,1});
        Polynomial pol3 = new Polynomial(new double[]{3,0,1});
        Polynomial pol4 = pol1.add(pol2);
        Polynomial pol5 = pol1.sub(pol2);
        Polynomial pol6 = pol1.mul(pol2);
        Polynomial pol7 = pol2.div(pol3);

        System.out.println(pol1);
        System.out.println(pol2);
        System.out.println(pol3);
        System.out.println(pol4);
        System.out.println(pol5);
        System.out.println(pol6);
        System.out.println(pol7);
        System.out.println(pol1.evaluate(3.5));
        System.out.println(pol1.derivative().derivative());
    }
}
