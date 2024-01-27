/*
package faceproject.faceanalysis;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;

    @PostConstruct
    public void init() {

    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private EntityManager em;

        public void dbInit() {
            String sampleDir = "data\\sample\\";

            Long sequence = 1L;
            while(true) {
                File file = new File(sampleDir + sequence);
                if(!file.exists()) break;


            }




            while()
        }
    }
}
*/
