package com.example.api_gateway.controllers;

import com.example.api_gateway.exceptions.UnsupportMathOperationException;
import com.example.api_gateway.services.MathOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

import static com.example.api_gateway.services.NumbeValidator.convertToDouble;
import static com.example.api_gateway.services.NumbeValidator.isZero;


@RestController
public class MathController {
    private final AtomicLong counter = new AtomicLong();
    private MathOperations operations = new MathOperations();

    @GetMapping(value = "/sum/{numberOne}/{numberTwo}")
    public Double sum(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        return operations.sum(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @GetMapping(value = "/sub/{numberOne}/{numberTwo}")
    public Double subtraction(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        return operations.subtraction(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @GetMapping(value = "/multiply/{numberOne}/{numberTwo}")
    public Double multiplication(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        return operations.multiplication(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @GetMapping(value = "/division/{numberOne}/{numberTwo}")
    public Double division(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        if (isZero(numberTwo)) {
            throw new UnsupportMathOperationException("numbers can't be divided by zero");
        }
        return operations.division(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @GetMapping(value = "/mean/{numberOne}/{numberTwo}")
    public Double mean(@PathVariable(value = "numberOne") String numberOne, @PathVariable(value = "numberTwo") String numberTwo) {
        return operations.mean(convertToDouble(numberOne), convertToDouble(numberTwo));
    }

    @GetMapping(value = "/sqrt/{numberOne}")
    public Double sqrt(@PathVariable(value = "numberOne") String numberOne) {
        return operations.sqrt(convertToDouble(numberOne));

    }


}
