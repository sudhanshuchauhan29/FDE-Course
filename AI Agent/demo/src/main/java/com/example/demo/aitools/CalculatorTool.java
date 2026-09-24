package com.example.demo.aitools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
public class CalculatorTool {
    @Tool(description= """
            Performs Arithmatic Calculations...
            Supports add,subtract, multiply, power, divide ,modulus
            """)
    public double calculate(
            @ToolParam(description = "Operation : Add, subtract, multiply, power, divide, modulus")
            String operation,
            @ToolParam(description = "First Number")
            double a,
            @ToolParam(description = "Second Number")
            double b)
    {
        System.out.println("Calculator Tool Called!");
        if (operation.equals("add"))
            return a + b;

        else if (operation.equals("subtract"))
            return a - b;

        else if (operation.equals("multiply"))
            return a * b;

        else if(operation.equals("power"))
            return Math.pow(a,b);

        else if (operation.equals("divide"))
        {
            if(b==0)
            {
                throw new IllegalArgumentException("Can not divide by 0");
            }
            else return a / b;
        }

        else if (operation.equals("modulus"))
        {
            if(b==0)
            {
                throw new IllegalArgumentException("Cannot calculate  modulus by 0");
            }
            return a % b;
        }

        else
            throw new IllegalArgumentException("Unsupported operation"+operation);

    }

}
