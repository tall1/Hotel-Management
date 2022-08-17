package unit;

import com.hotels.HotelApplication;
import com.hotels.entities.user.UserDTO;
import com.hotels.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HotelApplication.class)
public class SpringBootHelloWorldTests {

    @Autowired
    UserService userService;

    @Test
    public void contextLoads() {
        List<UserDTO> userList = userService.getAll();
        assertThat(userList.size()).isGreaterThan(0);
    }

}