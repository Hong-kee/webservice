package com.harry.webservice.springboot.web.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloResponseDtoTest {

    @Test
    void 롬복_기능_테스트() {
        // given
        String name = "test";
        Integer amount = 1000;

        // when
        HelloResponseDto helloResponseDto = HelloResponseDto.builder()
                .name(name)
                .amount(amount)
                .build();

        // then
        assertEquals(helloResponseDto.getName(), name);
        assertEquals(helloResponseDto.getAmount(), amount);
    }

}