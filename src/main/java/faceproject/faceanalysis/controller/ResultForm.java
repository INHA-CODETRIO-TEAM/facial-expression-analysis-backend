package faceproject.faceanalysis.controller;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ResultForm {
    private Long id;
    private int sequence;
    private BigDecimal angry;
    private BigDecimal fear;
    private BigDecimal happy;
    private BigDecimal sad;
    private BigDecimal surprise;
    private BigDecimal neutral;
}
