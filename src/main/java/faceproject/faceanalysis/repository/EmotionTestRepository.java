package faceproject.faceanalysis.repository;

import faceproject.faceanalysis.domain.EmotionTest;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmotionTestRepository {

    private final EntityManager em;

    public void save(EmotionTest emotionTest) {
        em.persist(emotionTest);
    }

    public EmotionTest findOne(Long id) {
        return em.find(EmotionTest.class, id);
    }
}
