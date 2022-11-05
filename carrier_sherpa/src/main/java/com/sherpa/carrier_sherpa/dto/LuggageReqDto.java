package com.sherpa.carrier_sherpa.dto;

import com.sherpa.carrier_sherpa.domain.enums.LuggageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class LuggageReqDto {

    private String start;

    private String destination;

    private String start_time;

    private String end_time;

    private String luggage_image_url;

    private LuggageType luggageType;


}
