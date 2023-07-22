package com.michalharasim.githublisting.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ApiException {
    private int status;
    private String message;
}
