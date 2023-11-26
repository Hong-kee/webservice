package com.harry.webservice.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class HelloResponseDto {

    private final String name;

    private final Integer amount;

}

