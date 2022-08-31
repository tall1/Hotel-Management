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
public class UserTests {

    @Autowired
    UserService userService;

    @Test
    public void testAllUsersNotEmpty() {
        List<UserDTO> userList = userService.getAll();
        assertThat(userList.size()).isGreaterThan(0);
    }

    @Test
    public void testSpecificUser() {
        boolean condition = userService.verifyEmailPass("talev142@gmail.com", "12345");
        assertThat(condition).isEqualTo(true);
        condition = userService.verifyEmailPass("talev142@gmail.com", "1");
        assertThat(condition).isEqualTo(false);
    }

    @Test
    public void testVerifyEmailPassword() {
        UserDTO user = userService.getUserById(1);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getEmail()).isEqualTo("talev142@gmail.com");
        assertThat(user.getHotelId()).isEqualTo(1);
    }

}