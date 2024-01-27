package faceproject.faceanalysis.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Result {
    @Id @GeneratedValue
    @Column(name = "result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private EmotionTest emotionTest;

    private int sequence;
    private String imageUrl;

    @Column(precision = 4, scale = 3)
    private BigDecimal angry;
    @Column(precision = 4, scale = 3)
    private BigDecimal fear;
    @Column(precision = 4, scale = 3)
    private BigDecimal happy;
    @Column(precision = 4, scale = 3)
    private BigDecimal sad;
    @Column(precision = 4, scale = 3)
    private BigDecimal surprise;
    @Column(precision = 4, scale = 3)
    private BigDecimal neutral;

    public Result(EmotionTest emotionTest, int sequence, String imageUrl, BigDecimal[] emotions) {
        this.emotionTest = emotionTest;
        this.sequence = sequence;
        this.imageUrl = imageUrl;

        this.happy = emotions[0];
        this.surprise = emotions[1];
        this.angry = emotions[2];
        this.fear = emotions[3];
        this.sad = emotions[4];
        this.neutral = emotions[5];
    }
}
