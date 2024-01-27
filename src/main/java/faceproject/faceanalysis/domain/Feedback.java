package faceproject.faceanalysis.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class Feedback {

    @Id @GeneratedValue
    @Column(name = "feedback_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    private EmotionTest emotionTest;

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


    public void setFeedback(EmotionTest emotionTest, BigDecimal angry, BigDecimal fear,
                           BigDecimal happy, BigDecimal sad,
                           BigDecimal surprise, BigDecimal neutral) {
        this.emotionTest = emotionTest;
        this.angry = angry;
        this.fear = fear;
        this.happy = happy;
        this.sad = sad;
        this.surprise = surprise;
        this.neutral = neutral;
    }
}
