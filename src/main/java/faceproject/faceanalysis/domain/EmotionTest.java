package faceproject.faceanalysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class EmotionTest {
    @Id @GeneratedValue
    @Column(name = "test_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sample_id")
    private Sample sample;

    @JsonIgnore
    @OneToOne(mappedBy = "emotionTest", fetch = FetchType.LAZY)
    private Feedback feedback;

    @JsonIgnore
    @OneToMany(mappedBy = "emotionTest")
    private List<Result> result = new ArrayList<>();

    private String faceUrl;

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public void setFaceUrl(String url) {
        this.faceUrl = url;
    }
}
