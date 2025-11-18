package com.vcb.master.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@Getter
@Setter
public class MatchInputDTO {
    private String rawInput;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date matchDate;

}
