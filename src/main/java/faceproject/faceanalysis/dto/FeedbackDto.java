package faceproject.faceanalysis.dto;

import lombok.Getter;


@Getter
public class FeedbackDto {
    private Long id;
    private int angry;
    private int fear;
    private int happy;
    private int sad;
    private int surprise;

    public FeedbackDto(Long id, int angry, int fear, int happy, int sad, int surprise) {
        this.id = id;
        this.angry = angry;
        this.fear = fear;
        this.happy = happy;
        this.sad = sad;
        this.surprise = surprise;
    }
}
