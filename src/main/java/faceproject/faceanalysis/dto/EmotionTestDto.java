package faceproject.faceanalysis.dto;

import lombok.Getter;

@Getter
public class EmotionTestDto {
    private Long id;
    private byte[] sampleImg;
    private String comment;

    public EmotionTestDto(Long id, byte[] sampleImg, String comment) {
        this.id = id;
        this.sampleImg = sampleImg;
        this.comment = comment;
    }
}
