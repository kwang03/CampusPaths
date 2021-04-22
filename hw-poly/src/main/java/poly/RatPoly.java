/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package poly;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * <b>RatPoly</b> represents an immutable single-variate polynomial expression. RatPolys are sums of
 * RatTerms with non-negative exponents.
 *
 * <p>Examples of RatPolys include "0", "x-10", and "x^3-2*x^2+5/3*x+3", and "NaN".
 */
// See RatNum's documentation for a definition of "immutable".
public final class RatPoly {

    /**
     * Holds all the RatTerms in this RatPoly.
     */
    private final List<RatTerm> terms;

    // Definitions:
    // For a RatPoly p, let C(p,i) be "p.terms.get(i).getCoeff()" and
    // E(p,i) be "p.terms.get(i).getExpt()"
    // length(p) be "p.terms.size()"
    // (These are helper functions that will make it easier for us
    // to write the remainder of the specifications. They are not
    // executable code; they just represent complex expressions in a
    // concise manner, so that we can stress the important parts of
    // other expressions in the spec rather than get bogged down in
    // the details of how we extract the coefficient for the 2nd term
    // or the exponent for the 5th term. So when you see C(p,i),
    // think "coefficient for the ith term in p".)
    //
    // Abstraction Function:
    // RatPoly, p, represents the polynomial equal to the sum of the
    // RatTerms contained in 'terms':
    // sum (0 <= i < length(p)): p.terms.get(i)
    // If there are no terms, then the RatPoly represents the zero
    // polynomial.
    //
    // Representation Invariant for every RatPoly p:
    // terms != null &&
    // forall i such that (0 <= i < length(p)), C(p,i) != 0 &&
    // forall i such that (0 <= i < length(p)), E(p,i) >= 0 &&
    // forall i such that (0 <= i < length(p) - 1), E(p,i) > E(p, i+1)
    // In other words:
    // * The terms field always points to some usable object.
    // * No term in a RatPoly has a zero coefficient.
    // * No term in a RatPoly has a negative exponent.
    // * The terms in a RatPoly are sorted in descending exponent order.
    // (It is implied that 'terms' does not contain any null elements by the
    // above
    // invariant.)

    /**
     * A constant holding a Not-a-Number (NaN) value of type RatPoly.
     */
    public static final RatPoly NaN = new RatPoly(RatTerm.NaN);

    /**
     * A constant holding a zero value of type RatPoly.
     */
    public static final RatPoly ZERO = new RatPoly();

    /**
     * @spec.effects Constructs a new Poly, "0".
     */
    public RatPoly() {
        terms = new ArrayList<RatTerm>();
        checkRep();
    }

    /**
     * @param rt the single term which the new RatPoly equals
     * @spec.requires {@code rt.getExpt() >= 0}
     * @spec.effects Constructs a new Poly equal to "rt". If rt.isZero(), constructs a "0" polynomial.
     */
    public RatPoly(RatTerm rt) {
        // TODO: Fill in this method, then remove the RuntimeException
        terms = new ArrayList<RatTerm>();
        if(!rt.isZero()){
            terms.add(rt);
        }
        checkRep();
    }

    /**
     * @param c the constant in the term which the new RatPoly equals
     * @param e the exponent in the term which the new RatPoly equals
     * @spec.requires {@code e >= 0}
     * @spec.effects Constructs a new Poly equal to "c*x^e". If c is zero, constructs a "0"
     * polynomial.
     */
    public RatPoly(int c, int e) {
        // TODO: Fill in this method, then remove the RuntimeException
        terms = new ArrayList<RatTerm>();
        if(!(c == 0)){
            terms.add(new RatTerm(new RatNum(c), e));
        }
        checkRep();
    }

    /**
     * @param rt a list of terms to be contained in the new RatPoly
     * @spec.requires 'rt' satisfies clauses given in rep. invariant
     * @spec.effects Constructs a new Poly using 'rt' as part of the representation. The method does
     * not make a copy of 'rt'.
     */
    private RatPoly(List<RatTerm> rt) {
        terms = rt;
        // The spec tells us that we don't need to make a copy of 'rt'
        checkRep();
    }

    /**
     * Returns the degree of this RatPoly.
     *
     * @return the largest exponent with a non-zero coefficient, or 0 if this is "0"
     * @spec.requires !this.isNaN()
     */
    public int degree() {
        // TODO: Fill in this method, then remove the RuntimeException
        if(!terms.isEmpty()){
            return terms.get(0).getExpt();
        }
        return 0;
    }

    /**
     * Gets the RatTerm associated with degree 'deg'
     *
     * @param deg the degree for which to find the corresponding RatTerm
     * @return the RatTerm of degree 'deg'. If there is no term of degree 'deg' in this poly, then
     * returns the zero RatTerm.
     * @spec.requires !this.isNaN()
     */
    public RatTerm getTerm(int deg) {
        // TODO: Fill in this method, then remove the RuntimeException
        //Invariant: term_0 ... term_{i-1} does not contain a term with deg degree where t_i is the ith term in the polynomial
        //           and we are looking at the ith term In other words, none of the terms we have looked at so far are of degree deg.
        for(RatTerm term : terms){
            if(term.getExpt() == deg){
                return term;
            }
        }
        return RatTerm.ZERO;
    }

    /**
     * Returns true if this RatPoly is not-a-number.
     *
     * @return true if and only if this has some coefficient = "NaN"
     */
    public boolean isNaN() {
        // TODO: Fill in this method, then remove the RuntimeException
        //Invariant: term_0 ... term_{i-1} does not contain a term that is NaN where t_i is the ith term
        //          in the polynomial and we are looking at the ith term. In other words, none of the terms
        //          we have looked at so far are NaN.
        for(RatTerm term : terms){
            if(term.isNaN()){
                return true;
            }
        }
        return false;
    }

    /**
     * Inserts a term into a sorted sequence of terms, preserving the sorted nature of the sequence. 
     * If a term with the given degree already exists, adds their coefficients (helper procedure).
     *
     * <p>Definitions: Let a "Sorted List<RatTerm>" be a List<RatTerm> V such that [1] V is sorted in
     * descending exponent order && [2] there are no two RatTerms with the same exponent in V && [3]
     * there is no RatTerm in V with a coefficient equal to zero
     *
     * <p>For a Sorted List<RatTerm> V and integer e, let cofind(V, e) be either the coefficient for a
     * RatTerm rt in V whose exponent is e, or zero if there does not exist any such RatTerm in V.
     * (This is like the coeff function of RatPoly.) We will write sorted(lst) to denote that lst is a
     * Sorted List<RatTerm>, as defined above.
     *
     * @param lst     the list into which newTerm should be inserted
     * @param newTerm the term to be inserted into the list
     * @spec.requires lst != null && sorted(lst)
     * @spec.modifies lst
     * @spec.effects sorted(lst_post) && (cofind(lst_post,newTerm.getExpt()) =
     * cofind(lst,newTerm.getExpt()) + newTerm.getCoeff())
     */
    private static void sortedInsert(List<RatTerm> lst, RatTerm newTerm) {
        // TODO: Fill in this method, then remove the RuntimeException
        // Note: Some of the provided code in this class relies on this method working as-specified.
        //Invariant: term_0, ..., term_{i - 1} in lst have exponents greater than newTerm's exponent where
        //           term_i is the ith term in lst.
        int i;
        for(i = 0; i < lst.size(); i++){
            RatTerm term = lst.get(i);
            if(term.getExpt() == newTerm.getExpt()){
                lst.set(i, newTerm.add(term));
                if(lst.get(i).equals(RatTerm.ZERO)){
                    lst.remove(i);
                }
                return;
            } else if(term.getExpt() < newTerm.getExpt()){
                break;
            }
        }
        if(!newTerm.isZero()) {
            lst.add(i, newTerm);
        }
    }

    /**
     * Return the additive inverse of this RatPoly.
     *
     * @return a RatPoly equal to "0 - this"; if this.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly negate() {
        // TODO: Fill in this method, then remove the RuntimeException
        List<RatTerm> lst = new ArrayList<RatTerm>();
        //Invariant: lst = [-term_0,...-term_{i-1}] where term_i is the ith term in this's terms. And we are
        //           currently looking at the ith term.
        for(RatTerm term: terms){
            lst.add(term.negate());
        }
        return new RatPoly(lst);
    }

    /**
     * Addition operation.
     *
     * @param p the other value to be added
     * @return a RatPoly, r, such that r = "this + p"; if this.isNaN() or p.isNaN(), returns some r
     * such that r.isNaN()
     * @spec.requires p != null
     */
    public RatPoly add(RatPoly p) {
        // TODO: Fill in this method, then remove the RuntimeException
        List<RatTerm> r = new ArrayList<RatTerm>(this.terms);
        //Invariant: The polynomial that r represents = this + p_0 + p_1 +...+ p_{i-1} where p_i is the ith term in p and we are currently looking at the ith term.
        for (RatTerm term : p.terms){
            RatTerm common = this.getTerm(term.getExpt());
            if(!common.isZero()){
                if(!common.add(term).equals(RatTerm.ZERO)) {
                    r.set(r.indexOf(common), common.add(term));
                }else{
                    r.remove(common);
                }
            }else{
                sortedInsert(r, term);
            }
        }
        return new RatPoly(r);
    }

    /**
     * Subtraction operation.
     *
     * @param p the value to be subtracted
     * @return a RatPoly, r, such that r = "this - p"; if this.isNaN() or p.isNaN(), returns some r
     * such that r.isNaN()
     * @spec.requires p != null
     */
    public RatPoly sub(RatPoly p) {
        // TODO: Fill in this method, then remove the RuntimeException
        return this.add(p.negate());
    }

    /**
     * Multiplication operation.
     *
     * @param p the other value to be multiplied
     * @return a RatPoly, r, such that r = "this * p"; if this.isNaN() or p.isNaN(), returns some r
     * such that r.isNaN()
     * @spec.requires p != null
     */
    public RatPoly mul(RatPoly p) {
        // TODO: Fill in this method, then remove the RuntimeException
        RatPoly r = new RatPoly();
        //Invariant: r = this * [p_0 + ... + p_{i-1}] where p_i is the ith term in p
        for(RatTerm term : p.terms){
            List<RatTerm> lst = new ArrayList<RatTerm>();
            for(RatTerm term2 : this.terms){
                sortedInsert(lst, term.mul(term2));
            }
            r = r.add(new RatPoly(lst));
        }
        return r;
    }

    /**
     * Truncating division operation.
     *
     * <p>Truncating division gives the number of whole times that the divisor is contained within the
     * dividend. That is, truncating division disregards or discards the remainder. Over the integers,
     * truncating division is sometimes called integer division; for example, 10/3=3, 15/2=7.
     *
     * <p>Here is a formal way to define truncating division: u/v = q, if there exists some r such
     * that:
     *
     * <ul>
     * <li>u = q * v + r<br>
     * <li>The degree of r is strictly less than the degree of v.
     * <li>The degree of q is no greater than the degree of u.
     * <li>r and q have no negative exponents.
     * </ul>
     * <p>
     * q is called the "quotient" and is the result of truncating division. r is called the
     * "remainder" and is discarded.
     *
     * <p>Here are examples of truncating division:
     *
     * <ul>
     * <li>"x^3-2*x+3" / "3*x^2" = "1/3*x" (with r = "-2*x+3")
     * <li>"x^2+2*x+15 / 2*x^3" = "0" (with r = "x^2+2*x+15")
     * <li>"x^3+x-1 / x+1 = x^2-x+2 (with r = "-3")
     * </ul>
     *
     * @param p the divisor
     * @return the result of truncating division, {@code this / p}. If p = 0 or this.isNaN() or
     * p.isNaN(), returns some q such that q.isNaN().
     * @spec.requires p != null
     */
    public RatPoly div(RatPoly p) {
        // TODO: Fill in this method, then remove the RuntimeException
        if(p.equals(ZERO) || p.isNaN() || this.isNaN()){
            return NaN;
        }
        RatPoly r = new RatPoly(this.terms);
        RatPoly q = new RatPoly();
        RatTerm t = p.terms.get(0);
        //Invariant: this = q * p + r
        while(!r.equals(ZERO) && r.degree() >= t.getExpt()){
            RatTerm d = r.terms.get(0).div(t);
            RatPoly dPoly = new RatPoly(d);
            q = q.add(dPoly);
            RatPoly m = dPoly.mul(p);
            r = r.sub(m);
        }
        return q;
    }

    /**
     * Return the derivative of this RatPoly.
     *
     * @return a RatPoly, q, such that q = dy/dx, where this == y. In other words, q is the derivative
     * of this. If this.isNaN(), then return some q such that q.isNaN().
     * <p>The derivative of a polynomial is the sum of the derivative of each term.
     */
    public RatPoly differentiate() {
        // TODO: Fill in this method, then remove the RuntimeException
        if(this.isNaN()){
            return NaN;
        }
        List<RatTerm> lst = new ArrayList<RatTerm>();
        //Invariant: lst = [term_0',...,term_{i-1}'] where t_{i}' is the derivative of the ith term in this polynomial and we
        //           are currently looking at the ith element in the for each loop.
        for(RatTerm term : this.terms){
            RatTerm derivative = term.differentiate();
            if(!derivative.isZero()){
                lst.add(derivative);
            }
        }
        return new RatPoly(lst);
    }

    /**
     * Returns the antiderivative of this RatPoly.
     *
     * @param integrationConstant the constant of integration to use when computing the antiderivative
     * @return a RatPoly, q, such that dq/dx = this and the constant of integration is
     * "integrationConstant" In other words, q is the antiderivative of this. If this.isNaN() or
     * integrationConstant.isNaN(), then return some q such that q.isNaN().
     * <p>The antiderivative of a polynomial is the sum of the antiderivative of each term plus
     * some constant.
     * @spec.requires integrationConstant != null
     */
    public RatPoly antiDifferentiate(RatNum integrationConstant) {
        // TODO: Fill in this method, then remove the RuntimeException
        if(this.isNaN()){
            return NaN;
        }
        List<RatTerm> lst = new ArrayList<RatTerm>();
        //Invariant: lst = [term_0',...,term_{i-1}'] where t_{i}' is the integral of the ith term in this polynomial and we
        //           are currently looking at the ith element in the for each loop.
        for(RatTerm term : this.terms){
            lst.add(term.antiDifferentiate());
        }
        if(!integrationConstant.equals(RatNum.ZERO)) {
            lst.add(new RatTerm(integrationConstant, 0));
        }
        return new RatPoly(lst);
    }

    /**
     * Returns the integral of this RatPoly, integrated from lowerBound to upperBound.
     *
     * <p>The Fundamental Theorem of Calculus states that the definite integral of f(x) with bounds a
     * to b is F(b) - F(a) where dF/dx = f(x) NOTE: Remember that the lowerBound can be higher than
     * the upperBound.
     *
     * @param lowerBound the lower bound of integration
     * @param upperBound the upper bound of integration
     * @return a double that is the definite integral of this with bounds of integration between
     * lowerBound and upperBound. If this.isNaN(), or either lowerBound or upperBound is
     * Double.NaN, return Double.NaN.
     */
    public double integrate(double lowerBound, double upperBound) {
        // TODO: Fill in this method, then remove the RuntimeException
        RatPoly integral = antiDifferentiate(RatNum.ZERO);
        return integral.eval(upperBound) - integral.eval(lowerBound);
    }

    /**
     * Returns the value of this RatPoly, evaluated at d.
     *
     * @param d the value at which to evaluate this polynomial
     * @return the value of this polynomial when evaluated at 'd'. For example, "x+2" evaluated at 3
     * is 5, and "x^2-x" evaluated at 3 is 6. If (this.isNaN() == true), return Double.NaN.
     */
    public double eval(double d) {
        // TODO: Fill in this method, then remove the RuntimeException
        double result = 0;
        //result = [term_0.eval(d) + ... + term_{i - 1}.eval(d)] where t_{i}.eval(d) is the ith term of the polynomial
        //         evaluated at d and we are currently looking at the ith term.
        for(RatTerm term : this.terms){
            result += term.eval(d);
        }
        return result;
    }

    /**
     * Returns a string representation of this RatPoly. Valid example outputs include
     * "x^17-3/2*x^2+1", "-x+1", "-1/2", and "0".
     *
     * @return a String representation of the expression represented by this, with the terms sorted in
     * order of degree from highest to lowest.
     * <p>There is no whitespace in the returned string.
     * <p>If the polynomial is itself zero, the returned string will just be "0".
     * <p>If this.isNaN(), then the returned string will be just "NaN".
     * <p>The string for a non-zero, non-NaN poly is in the form "(-)T(+|-)T(+|-)...", where "(-)"
     * refers to a possible minus sign, if needed, and "(+|-)" refers to either a plus or minus
     * sign. For each term, T takes the form "C*x^E" or "C*x" where {@code C > 0}, UNLESS: (1) the
     * exponent E is zero, in which case T takes the form "C", or (2) the coefficient C is one, in
     * which case T takes the form "x^E" or "x". In cases were both (1) and (2) apply, (1) is
     * used.
     */
    @Override
    public String toString() {
        if(terms.size() == 0) {
            return "0";
        }
        if(isNaN()) {
            return "NaN";
        }
        StringBuilder output = new StringBuilder();
        boolean isFirst = true;
        for(RatTerm rt : terms) {
            if(isFirst) {
                isFirst = false;
                output.append(rt.toString());
            } else {
                if(rt.getCoeff().isNegative()) {
                    output.append(rt.toString());
                } else {
                    output.append("+" + rt.toString());
                }
            }
        }
        return output.toString();
    }

    /**
     * Builds a new RatPoly, given a descriptive String.
     *
     * @param polyStr a string of the format described in the @spec.requires clause.
     * @return a RatPoly p such that p.toString() = polyStr
     * @spec.requires 'polyStr' is an instance of a string with no spaces that expresses a poly in the
     * form defined in the toString() method.
     * <p>Valid inputs include "0", "x-10", and "x^3-2*x^2+5/3*x+3", and "NaN".
     */
    public static RatPoly valueOf(String polyStr) {

        List<RatTerm> parsedTerms = new ArrayList<>();

        // First we decompose the polyStr into its component terms;
        // third arg orders "+" and "-" to be returned as tokens.
        StringTokenizer termStrings = new StringTokenizer(polyStr, "+-", true);

        boolean nextTermIsNegative = false;
        while(termStrings.hasMoreTokens()) {
            String termToken = termStrings.nextToken();

            if(termToken.equals("-")) {
                nextTermIsNegative = true;
            } else if(termToken.equals("+")) {
                nextTermIsNegative = false;
            } else {
                // Not "+" or "-"; must be a term
                RatTerm term = RatTerm.valueOf(termToken);

                // at this point, coeff and expt are initialized.
                // Need to fix coeff if it was preceeded by a '-'
                if(nextTermIsNegative) {
                    term = term.negate();
                }

                // accumulate terms of polynomial in 'parsedTerms'
                sortedInsert(parsedTerms, term);
            }
        }
        return new RatPoly(parsedTerms);
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return
     */
    @Override
    public int hashCode() {
        // all instances that are NaN must return the same hashcode;
        if(this.isNaN()) {
            return 0;
        }
        return terms.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @param obj the object to be compared for equality
     * @return true if and only if 'obj' is an instance of a RatPoly and 'this' and 'obj' represent
     * the same rational polynomial. Note that all NaN RatPolys are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RatPoly) {
            RatPoly rp = (RatPoly) obj;

            // special case: check if both are NaN
            if(this.isNaN() && rp.isNaN()) {
                return true;
            } else {
                return terms.equals(rp.terms);
            }
        } else {
            return false;
        }
    }

    /**
     * Throws an exception if the representation invariant is violated.
     */
    private void checkRep() {
        assert (terms != null);

        for(int i = 0; i < terms.size(); i++) {
            assert (!terms.get(i).getCoeff().equals(new RatNum(0))) : "zero coefficient";
            assert (terms.get(i).getExpt() >= 0) : "negative exponent";

            if(i < terms.size() - 1)
                assert (terms.get(i + 1).getExpt() < terms.get(i).getExpt()) : "terms out of order";
        }
    }
}
