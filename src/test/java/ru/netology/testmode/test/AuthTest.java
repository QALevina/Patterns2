package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
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

        var registeredUser = getRegisteredUser("active");

        $x("//input[contains(@name,'login')]").setValue(registeredUser.getLogin());
        $x("//input[contains(@name,'password')]").setValue(registeredUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__title']").shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }
    @Test
    @DisplayName("Should get error message if login with not registered user")
    void IfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        $x("//input[contains(@name,'login')]").setValue(notRegisteredUser.getLogin());
        $x("//input[contains(@name,'password')]").setValue(notRegisteredUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__title']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void IfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//input[contains(@name,'login')]").setValue(blockedUser.getLogin());
        $x("//input[contains(@name,'password')]").setValue(blockedUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__title']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));

    }

    @Test
    @DisplayName("If login is wrong - show a message")
    void IfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//input[contains(@name,'login')]").setValue((wrongLogin));
        $x("//input[contains(@name,'password')]").setValue(registeredUser.getPassword());
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__title']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void IfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//input[contains(@name,'login')]").setValue((registeredUser.getLogin()));
        $x("//input[contains(@name,'password')]").setValue(wrongPassword);
        $x("//button[contains(@data-test-id,'action-login')]").click();
        $x("//div[@class='notification__title']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }
}

