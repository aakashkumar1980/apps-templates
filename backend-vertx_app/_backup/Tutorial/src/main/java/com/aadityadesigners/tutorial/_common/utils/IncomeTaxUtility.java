package com.aadityadesigners.tutorial._common.utils;

public class IncomeTaxUtility {
    
    public Integer getTaxRate(Integer age) {
        return (age>=60)? 12:25;
    }
    public Integer getTaxableIncome(Integer deductions, Integer grossSalary) {
        return grossSalary-deductions;
    }

    public Integer computeTaxAmount(Integer taxRate, Integer taxableIncome) {
        return ((Double)((Double.valueOf(taxRate+"")/100.0)*Double.valueOf(taxableIncome+""))).intValue();
    }
}
