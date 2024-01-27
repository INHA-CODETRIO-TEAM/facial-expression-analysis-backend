package faceproject.faceanalysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sample {
    @Id @GeneratedValue
    @Column(name = "sample_id")
    private Long id;

    private String imgUri;
    private String comment;

    @JsonIgnore
    @OneToMany(mappedBy = "sample")
    private List<EmotionTest> emotionTests = new ArrayList<>();


}
