package casedoitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.WebDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;
class LoginTest {
    private static TestBot testBot;
    private static WebDriver driver;

    @BeforeAll
    static void setUp(){
        String URL = "https://www.casedoit.com/";
        testBot = new TestBot(URL);
        driver = testBot.getDriver();
    }

    @AfterAll
    static void quit(){
        testBot.initializeWait();
        testBot.quit();
    }

    @Test
    void testValidLogin() {
        String valid_email = "tekyigit.defne@gmail.com";
        String valid_password = "abcd1234*";
        testBot.login(valid_email,valid_password);
        assertTrue(testBot.isOnDashboard());
    }
    @ParameterizedTest
    @CsvSource({
            "lara.özduman@invalid.com , abcd1234*",
            "tekyigit.defne@gmail.com , invalid",
            "lara.özduman@invalid.com , invalid"
    })
    void testInvalidLogin(String email, String password){
        testBot.login(email,password);
        assertTrue(testBot.isErrorMessageDisplayed());
    }
    @Test
    void testEmptyMail() {
        String valid_email = "";
        String valid_password = "abcd1234*";
        testBot.login(valid_email,valid_password);
        testBot.initializeWait();
        assertTrue(testBot.isEmptyEmailMessageDisplayed());
    }

    @Test
    void testEmptyPassword() {
        String valid_email = "tekyigit.defne@gmail.com";
        String valid_password = "";
        testBot.login(valid_email,valid_password);
        testBot.initializeWait();
        assertTrue(testBot.isErrorMessageDisplayed());
    }
    @Test
    void testEmptyInput() {
        String empty_email = "";
        String empty_password = "";
        testBot.login(empty_email,empty_password);
        testBot.initializeWait();
        assertTrue(testBot.isEmptyEmailMessageDisplayed());
        assertTrue(testBot.isErrorMessageDisplayed());
    }


}