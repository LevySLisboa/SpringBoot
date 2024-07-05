package com.example.api_gateway.services;

import com.example.api_gateway.exceptions.UnsupportMathOperationException;

public class NumbeValidator {

    public static Double convertToDouble(String strNumber) {
        if (strNumber == null) return 0d;
        String number = strNumber.replaceAll(",", ".");
        if (isNumeric(number)) {
            return Double.parseDouble(number);
        } else {
            throw new UnsupportMathOperationException("Please set a numeric value");
        }
    }

    private static boolean isNumeric(String strNumber) {
        if (strNumber == null) return false;
        String number = strNumber.replaceAll(",", ".");
        return number.matches("[-+]?\\d*\\.?\\d+");

    }

        public static boolean isZero(String strNumber){
            if (strNumber == null) return false;
            return convertToDouble(strNumber) == 0;
        }

}
