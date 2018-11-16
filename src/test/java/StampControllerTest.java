import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jarics.preemtive.Application;
import com.jarics.preemtive.Captain;
import com.jarics.preemtive.JsonUtil;
import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class StampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));


    @Before
    public void before() {
    }

    @After
    public void after() {
    }


    @Test
    public void testUpdateCaptainOptimisticException() throws Exception {
        Captain captain = generate();
        captain = create(captain);

        try {

            Captain captain1 = getCaptain(captain.getId());
            captain1.setAge(100);

            mockMvc.perform(post("/api/captain").contentType(contentType).
                content(JsonUtil.convertObjectToJsonBytes(captain1)))
                .andExpect(status().isOk());

            captain1.setAge(200);

            mockMvc.perform(post("/api/captain").contentType(contentType).
                    content(JsonUtil.convertObjectToJsonBytes(captain)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string("Concurrency Exception"))
                    .andDo(print());
        } catch (Exception e) {
            //sink it
        }

        deleteCaptain( captain.getId() );
    }


    private void deleteCaptain(long id) throws Exception {
        String wUrl = "/api/captain/" + id;
        mockMvc.perform(delete(wUrl)).andExpect(status().isOk()).andExpect(content().
            string(org.hamcrest.Matchers.containsString( Long.toString(id))));
    }



    private Captain create(Captain captain) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/api/captain").contentType(contentType)
                .content(JsonUtil.convertObjectToJsonBytes(captain))).andReturn();
        Captain captain1 = (Captain) JsonUtil
            .convertJsonBytesToObject(mvcResult.getResponse().getContentAsString(),
                Captain.class);
        return captain1;
    }

    private Captain getCaptain(long id) throws Exception {
        String wUrl = "/api/captain/" + id;
        MvcResult wMvcResult = mockMvc.perform(get(wUrl)).andExpect(status().isOk()).andExpect(content().
                string(org.hamcrest.Matchers.containsString( Long.toString(id) ) )).andReturn();
        Captain wFtp = (Captain) JsonUtil
                .convertJsonBytesToObject(wMvcResult.getResponse().getContentAsString(),
                        Captain.class);
        return wFtp;
    }



    private Captain generate() {
        Captain captain = new Captain();
        captain.setAge(14);
        return captain;
    }


}
