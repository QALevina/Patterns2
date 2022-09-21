package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.*;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {

        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")

    void shouldSuccessfulLoginIfRegisteredActiveUser() {

        var registeredUser = getUser("active");

        $x("//input[contains(@name,'login')]").setValue(registeredUser.getLogin());
        $x("//input[contains(@name,'password')]").setValue(registeredUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//*[@id='root']").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//input[contains(@name,'login')]").setValue(notRegisteredUser.getLogin());
        $x("//input[contains(@name,'password')]").setValue(notRegisteredUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getUser("blocked");
        $x("//input[contains(@name,'login')]").setValue(blockedUser.getLogin());
        $x("//input[contains(@name,'password')]").setValue(blockedUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("If login is wrong - show a message")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getUser("active");
        var wrongLogin = getRandomLogin();
        $x("//input[contains(@name,'login')]").setValue((wrongLogin));
        $x("//input[contains(@name,'password')]").setValue(registeredUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getUser("active");
        var wrongPassword = getRandomPassword();
        $x("//input[contains(@name,'login')]").setValue((registeredUser.getLogin()));
        $x("//input[contains(@name,'password')]").setValue(wrongPassword);
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль"));

    }
}



