package binar.box.Controller;

import binar.box.dto.LockStepOneDTO;
import binar.box.rest.LockController;
import binar.box.service.LockService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { LockController.class }, secure = false)
public class LockControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private LockService lockService;

    @Test
    public void addUserLockFirstStep() throws Exception {
        var lockStepOneDTO = new LockStepOneDTO();
        lockStepOneDTO.setLockTemplate(2L);
        lockStepOneDTO.setMessage("LOCK TEXT");
        lockStepOneDTO.setPrivateLock(false);

        Mockito.when(lockService.createUserLock(Mockito.any(LockStepOneDTO.class)))
                .thenReturn(lockStepOneDTO);
        Gson gson = new Gson();
        String Json = gson.toJson(new LockStepOneDTO());

            mockMvc.perform(MockMvcRequestBuilders.post("/api/v0/lock")
                    .contentType(MediaType.APPLICATION_JSON_VALUE).content(Json))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(jsonPath("$.lockTemplate").value("2"))
                    .andExpect(jsonPath("$.message").value("LOCK TEXT"))
                    .andExpect(jsonPath("$.privateLock").value("false"));
    }
}
