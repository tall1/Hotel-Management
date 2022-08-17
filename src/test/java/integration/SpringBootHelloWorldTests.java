package integration;

import com.hotels.HotelApplication;
import com.hotels.entities.user.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = HotelApplication.class)
public class SpringBootHelloWorldTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() {
        UserDTO userDto = this.restTemplate.getForObject("http://localhost:" + port + "/users/1", UserDTO.class);
        assertThat(userDto.getId()).isEqualTo(1);
    }
}