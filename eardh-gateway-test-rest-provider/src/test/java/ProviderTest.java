import com.eardh.gateway.provider.controller.UserController;
import org.junit.jupiter.api.Test;

/**
 * @author eardh
 * @date 2023/4/14 13:47
 */
public class ProviderTest {


    @Test
    void t1() throws NoSuchMethodException {
        System.out.println(UserController.class.getDeclaredMethod("login", String.class, String.class).toString());
    }

}

