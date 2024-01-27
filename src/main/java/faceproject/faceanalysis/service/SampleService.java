package faceproject.faceanalysis.service;

import faceproject.faceanalysis.domain.Sample;
import faceproject.faceanalysis.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public Long randomSample() {
        Long sampleCount = sampleRepository.getSampleCount();
        long randomVal = (long) (Math.random() * sampleCount);

        return randomVal + 1;
    }

    public String getSampleImage(Long id) throws Exception {
        Sample sample = sampleRepository.findOne(id);
        return sample.getImgUri();
    }

    public String getSampleComment(Long id) throws Exception {
        Sample sample = sampleRepository.findOne(id);
        return sample.getComment();
    }
}
