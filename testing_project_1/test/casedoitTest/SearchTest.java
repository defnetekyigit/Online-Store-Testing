package casedoitTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
class SearchTest {
    private static TestBot testBot;
    private static WebDriver driver;
    static List<String> search_list;
    @BeforeAll
    static void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\tekyi\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
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
    void testValidSearch() {
        testBot.search("AirPods");
        search_list = testBot.getSearchResults();
        for (String result : search_list) {
            assertTrue(result.toLowerCase().contains("airpods"));
        }
    }
    @Test
    void testMultiWordSearch(){
        testBot.search("Airpods Kılıf");
        search_list = testBot.getSearchResults();
        for (String result : search_list) {
            assertTrue(result.toLowerCase().contains("airpods kılıf"));
        }
    }
    @Test
    void testPartialMatchSearch(){
        testBot.search("Mi");
        search_list = testBot.getSearchResults();
        for (String result : search_list) {
            assertTrue(result.toLowerCase().contains("mi"));
        }
    }


    @Test
    void testInvalidSearch() {
        testBot.search("nonexistent");
        search_list = testBot.getSearchResults();
        assertEquals(0, search_list.size());
    }
    @Test
    void testEmptySearch() { //While doing empty search, it returns 2238 items(all the items)
        testBot.search("");
        search_list = testBot.getSearchResults();
        assertEquals(0, search_list.size());
    }
    @Test
    void testOneSearch(){
        testBot.search("a");
        testBot.initializeWait();
        String enteredText = driver.findElement(By.id("input_search-box-input-comp-krswnht1")).getAttribute("value");
        System.out.println("Search input length : 1 Actual Searched length: "+enteredText.length());
        assertEquals(1, enteredText.length());
    }
    @Test
    void testBoundaryBelowMaxLength() {
        String belowMaxLengthString = "a".repeat(99);
        testBot.search(belowMaxLengthString);
        testBot.initializeWait();
        String enteredText = driver.findElement(By.id("input_search-box-input-comp-krswnht1")).getAttribute("value");
        System.out.println("Search input length : 99 Actual Searched length: "+enteredText.length());
        assertEquals(99, enteredText.length());
    }
    @Test
    void testMaxLength() {
        String MaxLengthString = "a".repeat(100);
        testBot.search(MaxLengthString);
        testBot.initializeWait();
        String enteredText = driver.findElement(By.id("input_search-box-input-comp-krswnht1")).getAttribute("value");
        System.out.println("Search input length : 100 Actual Searched length: "+enteredText.length());
        assertEquals(100, enteredText.length());
    }
    @Test
    void testBoundaryAboveMaxLength() {
        String aboveMaxLengthString = "a".repeat(101);
        testBot.search(aboveMaxLengthString);
        testBot.initializeWait();
        String enteredText = driver.findElement(By.id("input_search-box-input-comp-krswnht1")).getAttribute("value");
        System.out.println("Search input length : 101 Actual Searched length: "+enteredText.length());
        assertEquals(100, enteredText.length());
    }
    @Test
    void testVastlyAboveMaxLength() {
        String vastlyAboveMaxLengthString = "a".repeat(200);
        testBot.search(vastlyAboveMaxLengthString);
        testBot.initializeWait();
        String enteredText = driver.findElement(By.id("input_search-box-input-comp-krswnht1")).getAttribute("value");
        System.out.println("Search input length : 200 Actual Searched length: "+enteredText.length());
        assertEquals(100, enteredText.length());
    }
    @ParameterizedTest //While searching "*", it returns 2238 items(all the items)
    @ValueSource(strings = {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "{", "}", ":", "\"", "<", ">", "?", ",", ".", "/", ";", "'", "[", "]", "\\", "-"})
    void testSpecialChar(String specialChar) {
        testBot.search(specialChar);
        search_list = testBot.getSearchResults();
        assertEquals(0, search_list.size());
    }

}