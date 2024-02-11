package com.cyolo.frequentword.exception;

import lombok.Data;

@Data
public class InvalidInputException extends Exception{
    private String message;


}
