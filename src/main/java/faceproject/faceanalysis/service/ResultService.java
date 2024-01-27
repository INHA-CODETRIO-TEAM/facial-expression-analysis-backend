package faceproject.faceanalysis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import faceproject.faceanalysis.domain.Result;
import faceproject.faceanalysis.dto.EmotionSetDto;
import faceproject.faceanalysis.repository.EmotionTestRepository;
import faceproject.faceanalysis.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;
    private final EmotionTestRepository emotionTestRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${server-link.model}")
    private String modelServerUrl;

    public int savePhoto(Long id, List<MultipartFile> files) {

        String fileDir = uploadDir + "/data/faceImage/" + id;

        File folder = new File(fileDir);
        if (folder.exists()) {
            return -1;
        }
        boolean result = folder.mkdirs();
        if(!result) return -2;

        try {
            for(int i = 0; i < files.size(); i++) {
                String filePath = fileDir + "/" + (i+1) + ".jpg";
                files.get(i).transferTo(new File(filePath));
            }
            return 0;
        } catch (IOException e) {
            return -2;
        }

    }

    @Transactional
    public int sendPhoto(Long id) throws IOException {
        String dirPath = "data/faceImage/" + id + "/";
        for(int i = 0; i < 5; i++) {
            BigDecimal[] faceResult = analyzeImage(dirPath + (i+1) + ".jpg");
            if(faceResult == null) {
                return -1;
            }
            Result result = new Result(
                    emotionTestRepository.findOne(id), i+1,
                    dirPath + (i+1) + ".jpg", faceResult);
            resultRepository.save(result);
        }
        return 0;
    }


    public BigDecimal[] analyzeImage(String imagePath) throws IOException {
        // String modelServerUrl = "http://localhost:5000/detect_and_classify";
        String fullPath = uploadDir + "/" + imagePath;

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Resource image = new FileSystemResource(fullPath);

        body.add("image", image);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                modelServerUrl, HttpMethod.POST, requestEntity, String.class);

        String responseBody = response.getBody();

        BigDecimal[] result = parseResult(responseBody);
        return result;
    }


    public int selectCentralSequence(Long id) {
        int sequence = 0;
        BigDecimal minDispersion = new BigDecimal("10000");

        EmotionSet emotionSet = new EmotionSet();
        List<Result> results = resultRepository.findByTestId(id);
        emotionSet.calculateAverage(results);


        for(Result r : results) {
            BigDecimal temp = emotionSet.calculateDispersion(r);
            if(temp.compareTo(minDispersion) < 0) {
                sequence = r.getSequence();
                minDispersion = temp;
            }
        }

        return sequence;
    }

    private static BigDecimal[] parseResult(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            double[] doubleArray = objectMapper.readValue(jsonResponse, double[].class);
            BigDecimal[] bigDecimalArray = new BigDecimal[doubleArray.length];

            for (int i = 0; i < doubleArray.length; i++) {
                bigDecimalArray[i] = BigDecimal.valueOf(doubleArray[i]).setScale(3, RoundingMode.HALF_EVEN);
            }

            return bigDecimalArray;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Long saveResult(Result result) {
        resultRepository.save(result);
        return result.getId();
    }

    public EmotionSetDto getResult(Long id) {
        List<Result> results = resultRepository.findByTestId(id);
        EmotionSet emotionSet = new EmotionSet();
        emotionSet.calculateAverage(results);

        return new EmotionSetDto(
                emotionSet.getAngry(), emotionSet.getFear(),
                emotionSet.getHappy(), emotionSet.getSad(),
                emotionSet.getSurprise(), emotionSet.getNeutral());
    }
}
