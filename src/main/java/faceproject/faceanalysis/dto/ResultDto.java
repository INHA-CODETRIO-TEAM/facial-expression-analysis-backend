package faceproject.faceanalysis.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ResultDto {
    private byte[] faceImg;
    private byte[] sampleImg;

    private EmotionSetDto result;
    private EmotionSetDto feedback;

    private BigDecimal deviation;

    public ResultDto(
            byte[] faceImg, byte[] sampleImg,
            EmotionSetDto result, EmotionSetDto feedback,
            BigDecimal deviation) {

        this.faceImg = faceImg;
        this.sampleImg = sampleImg;
        this.result = result;
        this.feedback = feedback;
        this.deviation = deviation;
    }
}
