package faceproject.faceanalysis.repository;

import faceproject.faceanalysis.domain.Sample;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SampleRepository {

    private final EntityManager em;

    public Sample findOne(Long id) {
        return em.find(Sample.class, id);
    }

    public Long getSampleCount() {
        return em.createQuery("select id from Sample").getResultStream().count();
    }
}
