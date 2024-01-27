package faceproject.faceanalysis.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class EmotionSetDto {
    private BigDecimal angry;
    private BigDecimal fear;
    private BigDecimal happy;
    private BigDecimal sad;
    private BigDecimal surprise;
    private BigDecimal neutral;

    public EmotionSetDto(BigDecimal angry, BigDecimal fear,
                         BigDecimal happy, BigDecimal sad,
                         BigDecimal surprise, BigDecimal neutral) {
        this.angry = angry;
        this.fear = fear;
        this.happy = happy;
        this.sad = sad;
        this.surprise = surprise;
        this.neutral = neutral;
    }
}
