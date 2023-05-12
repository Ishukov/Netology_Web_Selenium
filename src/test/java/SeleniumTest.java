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
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void shouldHappyPath() {

        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79033777222");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id = 'order-success']")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldNameInputInvalidWithEmptyFields() {
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldPhoneInputInvalidWithEmptyFields() {
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldCheckboxInputInvalidWithEmptyFields() {
        String expected = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getCssValue("color");

        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).getCssValue("color");

        Assertions.assertNotEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/name.csv")
    void shouldNameInputValidation(String name, String expected) {

        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys(name);
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79088111222");
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/phone.csv")
    void shouldPhoneInputValidation(String phone, String expected) {

        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys("Иван Петров");
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys(phone);
        driver.findElement(By.cssSelector("[class='checkbox__box']")).click();
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldWithOutCheckBox() {
        String expected = driver.findElement(By.cssSelector("[data-test-id='agreement']")).getCssValue("color");

        driver.findElement(By.cssSelector("[data-test-id = 'name'] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id = 'phone'] input")).sendKeys("+79033777222");
        driver.findElement(By.cssSelector("[type = 'button']")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).getCssValue("color");

        Assertions.assertNotEquals(expected, actual);
    }
}
