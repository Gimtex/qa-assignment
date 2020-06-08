import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FirefoxTest {

    WebDriver driver = new FirefoxDriver();
    final String url = "http://localhost/";
    WebDriverWait wait = new WebDriverWait(driver, 15);
    private static boolean setUpIsDone = false;

    //TODO: should prepare after method to clear all test data

    @Before
    public void setUp() {
        if (setUpIsDone) {
            return;
        }

        // Ensure test use cases

        //BLOG AUTHENTICATION
        openPage();
        driver.manage().window().maximize();
        authentication();

        driver.findElement(By.linkText("Posts")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Categories")));
        driver.findElement(By.linkText("Categories")).click();

        if (!isElementPresent(By.linkText("Category 1"))) {
            driver.findElement(By.id("tag-name")).sendKeys("Category 1");
            driver.findElement(By.id("tag-slug")).sendKeys("Category 1");
            driver.findElement(By.id("submit")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Category 1")));
        }

        if (!isElementPresent(By.linkText("Category 2"))) {
            driver.findElement(By.id("tag-name")).sendKeys("Category 2");
            driver.findElement(By.id("tag-slug")).sendKeys("Category 2");
            driver.findElement(By.id("submit")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Category 2")));
        }


        // POST / DISPLAY ON FRONT PAGE
        for (int i = 1; i < 3; i++) {
            driver.findElement(By.cssSelector(".menu-icon-post > .wp-menu-name")).click();
            driver.findElement(By.linkText("Add New")).click();

            if (i == 1) {
                driver.findElement(By.cssSelector(".components-modal__header")).findElement(By.tagName("button")).click();
            }

            driver.findElement(By.id("post-title-0")).click();
            driver.findElement(By.id("post-title-0")).sendKeys("New Post Category ".concat(String.valueOf(i)));

            if (i == 1) {
                driver.findElement(By.xpath(
                        "/html/body/div[1]/div[2]/div[2]/div[1]/div[3]/div[1]/div/div/div[1]/div/div[2]/div[2]/div/div/div[3]/div[2]/h2/button"))
                        .click();
            }
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("inspector-checkbox-control-".concat(String.valueOf(i + 1)))));
            driver.findElement(By.id("inspector-checkbox-control-".concat(String.valueOf(i + 1)))).click();

            {
                driver.findElement(By.xpath(
                        "/html/body/div[1]/div[2]/div[2]/div[1]/div[3]/div[1]/div/div/div[1]/div/div[2]/div[1]/div[4]/div[2]/div/div/div[2]/div[2]/div/div[2]/div/div[1]"))
                        .click();
                final WebElement element1 = driver.findElement(By.xpath(
                        "/html/body/div[1]/div[2]/div[2]/div[1]/div[3]/div[1]/div/div/div[1]/div/div[2]/div[1]/div[4]/div[2]/div/div/div[2]/div[2]/div/div[2]/div/div[1]/p"));
                element1.sendKeys("Lorem ipsum ...");
            }

            driver.findElement(By.cssSelector(".editor-post-publish-panel__toggle")).click();
            driver.findElement(By.cssSelector(".editor-post-publish-button")).click();

            if (i != 2) {
                timeout(3000);
                driver.findElement(By.xpath(
                        "/html/body/div[1]/div[2]/div[2]/div[1]/div[3]/div[1]/div/div/div[1]/div/div[1]/div/a"))
                        .click();
            }
        }


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("View Post")));
        driver.findElement(By.linkText("View Post")).click();

        driver.close();
        setUpIsDone = true;

        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, 15);
    }

    private void authentication() {
        driver.findElement(By.linkText("Log in")).click();
        driver.findElement(By.id("user_login")).click();
        driver.findElement(By.id("user_login")).sendKeys("test");
        driver.findElement(By.id("user_pass")).click();
        driver.findElement(By.id("user_pass")).sendKeys("test");
        driver.findElement(By.id("wp-submit")).click();
    }


    @Test
    public void openBlogTest() {
        assertTrue(openPage());
        assertTrue(isElementPresent(By.id("page")));

        driver.close();
    }

    @Test
    public void categoryDisplayStateTest() {

        // TEST IF CATEGORIES ARE DISPLAYED AND ARE CLICKABLE

        openPage();

        assertTrue(isElementPresent(By.linkText("Category 1")));
        assertTrue(driver.findElement(By.linkText("Category 1")).isDisplayed());
        assertTrue(driver.findElement(By.linkText("Category 1")).isEnabled());

        assertTrue(isElementPresent(By.linkText("Category 2")));
        assertTrue(driver.findElement(By.linkText("Category 2")).isDisplayed());
        assertTrue(driver.findElement(By.linkText("Category 2")).isEnabled());

        assertFalse(isElementPresent(By.linkText("Category 3")));

        driver.close();
    }

    @Test
    public void postDisplayByCategoryTest() {
        openPage();
        driver.findElement(By.linkText("Category 1")).click();
        assertTrue(isSubElementPresent(By.linkText("New Post Category 1"), driver.findElement(By.id("main"))));
        assertFalse(isSubElementPresent(By.linkText("New Post Category 2"), driver.findElement(By.id("main"))));

        driver.findElement(By.linkText("Category 2")).click();
        assertTrue(isSubElementPresent(By.linkText("New Post Category 2"), driver.findElement(By.id("main"))));
        assertFalse(isSubElementPresent(By.linkText("New Post Category 1"), driver.findElement(By.id("main"))));

        driver.close();
    }

    @Test
    public void openPostTest() {
        openPage();

        assertFalse(isElementPresent(By.id("comment")));

        driver.findElement(By.id("main")).findElement(By.tagName("article")).findElement(By.tagName("a")).click();

        assertTrue(isElementPresent(By.id("comment")));

        driver.close();
    }

    @Test
    public void authUserCommentTest() {

        openPage();

        authentication();
        assertFalse(isElementPresent(By.id("login_error")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("wpadminbar")));
        final String commentContent = addCommentCommon();

        // set timeout to assure enough time between two blog posts
        timeout(11000);
        driver.findElement(By.id("submit")).click();

        final List<WebElement> comments = driver.findElement(By.tagName("ol")).findElements(By.tagName("li"));
        final WebElement comment = comments.get(comments.size() - 1);
        assertEquals(commentContent, comment.findElement(By.tagName("p")).getText());
        assertFalse(isSubElementPresent(By.cssSelector(".comment-awaiting-moderation"), comment));

        driver.close();
    }

    @Test
    public void noAuthUserCommentTest() {

        final String commentContent = addCommentCommon();

        if (isElementPresent(By.id("author"))) {
            driver.findElement(By.id("author")).sendKeys("John Doo");
        }

        if (isElementPresent(By.id("email"))) {
            driver.findElement(By.id("email")).sendKeys("john.doo@mailinator.com");
        }

        // set timeout to assure enough time between two blog posts
        timeout(11000);
        driver.findElement(By.id("submit")).click();

        final List<WebElement> comments = driver.findElement(By.tagName("ol")).findElements(By.tagName("li"));
        final WebElement comment = comments.get(comments.size() - 1);
        assertEquals(commentContent, comment.findElement(By.tagName("p")).getText());
        assertTrue(isSubElementPresent(By.cssSelector(".comment-awaiting-moderation"), comment));

        driver.close();
    }

    private void timeout(int millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String addCommentCommon() {
        openPage();

        final String commentContent = RandomStringUtils.randomAlphabetic(10);

        driver.findElement(By.id("main")).findElement(By.tagName("article")).findElement(By.tagName("a")).click();
        driver.findElement(By.id("comment")).sendKeys(commentContent);

        return commentContent;
    }

    public boolean isElementPresent(final By locatorKey) {
        try {
            driver.findElement(locatorKey);
            return true;
        } catch (final org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean isSubElementPresent(final By locatorKey, final WebElement element) {
        try {
            element.findElement(locatorKey);
            return true;
        } catch (final org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public boolean openPage() {
        try {
            driver.get(url);
            return true;
        } catch (final org.openqa.selenium.WebDriverException e) {
            return false;
        }
    }
}
