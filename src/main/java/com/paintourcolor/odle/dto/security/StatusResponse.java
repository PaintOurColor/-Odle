package com.paintourcolor.odle.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponse {
    private int statusCode;
    private String message;
}
