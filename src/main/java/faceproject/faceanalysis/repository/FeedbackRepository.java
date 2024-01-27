package faceproject.faceanalysis.repository;

import faceproject.faceanalysis.domain.Feedback;
import faceproject.faceanalysis.domain.Result;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FeedbackRepository {

    private final EntityManager em;

    public void save(Feedback feedback) {
        em.persist(feedback);
    }

    public List<Feedback> findByTestId(Long id) {
        return em.createQuery("select f from Feedback f where f.emotionTest.id = :test_id", Feedback.class)
                .setParameter("test_id", id)
                .getResultList();
    }
}
