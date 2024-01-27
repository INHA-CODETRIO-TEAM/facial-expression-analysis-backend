package faceproject.faceanalysis.service;

import faceproject.faceanalysis.domain.EmotionTest;
import faceproject.faceanalysis.domain.Feedback;
import faceproject.faceanalysis.domain.Result;
import faceproject.faceanalysis.domain.Sample;
import faceproject.faceanalysis.repository.EmotionTestRepository;
import faceproject.faceanalysis.repository.FeedbackRepository;
import faceproject.faceanalysis.repository.ResultRepository;
import faceproject.faceanalysis.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.random.RandomGenerator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmotionTestService {

    private final EmotionTestRepository emotionTestRepository;
    private final FeedbackRepository feedbackRepository;
    private final ResultRepository resultRepository;
    private final SampleRepository sampleRepository;

    @Value("${file.upload-dir}")
    private String baseDir;

    /**
     * 테스트 시작 함수
     */
    @Transactional
    public Long generateTest(EmotionTest emotionTest) {
        emotionTestRepository.save(emotionTest);
        return emotionTest.getId();
    }

    /**
     * 테스트 샘플을 무작위로 선정하는 함수
     */
    public void setTestSample(EmotionTest emotionTest, Long id) {
        Sample sample = sampleRepository.findOne(id);
        emotionTest.setSample(sample);
    }

    /**
     * 테스트 id를 통해 찾는 함수
     */
    public EmotionTest getTest(Long id) {
        return emotionTestRepository.findOne(id);
    }


    /**
     * 저장된 이미지 파일을 Byte로 가져오는 함수
     */
    public byte[] getImage(String imgUrl) throws Exception {
        String fullPath = baseDir + "/" + imgUrl;
        // 파일 경로를 Path 객체로 변환
        Path path = FileSystems.getDefault().getPath(fullPath);

        Path directoryPath = path.getParent();
        String directory = (directoryPath != null) ? directoryPath.toString() : "";
        String fileName = path.getFileName().toString();

        Path imagePath = Path.of(directory, fileName);
        Resource imgResource = new FileSystemResource(imagePath);

        return imgResource.getInputStream().readAllBytes();
    }

    /**
     * 오차를 구하는 함수
     */
    public BigDecimal calculateError(Long id) {
        List<Result> results = resultRepository.findByTestId(id);
        Feedback feedback = feedbackRepository.findByTestId(id).get(0);

        EmotionSet emotionSet = new EmotionSet();
        emotionSet.calculateAverage(results);

        return emotionSet.calculateError(feedback);
    }

    /**
     * 대표 이미지를 설정하는 함수
     */
    @Transactional
    public Long updateFaceUrl(Long id, int sequence) {
        EmotionTest emotionTest = emotionTestRepository.findOne(id);
        String imagePath = "data/faceImage/" + id + "/" + sequence + ".jpg";
        emotionTest.setFaceUrl(imagePath);
        return id;
    }
}
