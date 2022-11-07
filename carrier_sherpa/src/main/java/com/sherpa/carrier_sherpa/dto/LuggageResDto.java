package com.sherpa.carrier_sherpa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class LuggageResDto {

    private String luggageId;

    private String memberId;

    public LuggageResDto(String luggageId,String memberId){
        this.luggageId = luggageId;
        this.memberId = memberId;
    }
}
