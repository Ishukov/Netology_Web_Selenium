import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumTest {
    private WebDriver driver;


    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void shouldHappyPath() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79033777222");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id = 'order-success']")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/name.csv")
    void shouldNameInputValidation(String name, String expected) {
        driver.get("http://localhost:9999/");

        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79088111222");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'] [class='input__sub']")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/phone.csv")
    void shouldPhoneInputValidation(String phone, String expected) {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'] [class='input__sub']")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldWithOutCheckBox() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79033777222");
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String expected = "rgba(255, 92, 92, 1)";
        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement']  [class='checkbox__text']")).getCssValue("color");

        Assertions.assertEquals(expected, actual);
    }
}
