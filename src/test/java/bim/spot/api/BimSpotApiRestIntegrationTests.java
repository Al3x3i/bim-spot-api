package bim.spot.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class BimSpotApiRestIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private ResultActions resultActions;

    @Test
    @SneakyThrows
    void should_preview() {

        // GIVEN // WHEN
        resultActions = mockMvc.perform(get("/preview").param("region", "northeastern_africa"));

        // THEN
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

}
