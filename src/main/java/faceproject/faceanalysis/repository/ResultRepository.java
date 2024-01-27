package faceproject.faceanalysis.repository;

import faceproject.faceanalysis.domain.Result;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ResultRepository {

    private final EntityManager em;

    public void save(Result result) {
        em.persist(result);
    }

    public List<Result> findByTestId(Long id) {
        return em.createQuery("select r from Result r where r.emotionTest.id = :test_id", Result.class)
                .setParameter("test_id", id)
                .getResultList();
    }
}