package com.project.finsync.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
@AllArgsConstructor
public class ResponseBody {
    private HttpStatus httpStatus;
    private String message;
    private Object data;
}
