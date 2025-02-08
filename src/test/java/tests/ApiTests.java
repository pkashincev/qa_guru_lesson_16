package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import models.getUser.SingleUserResponse;
import models.getUser.UsersListResponse;
import models.setUser.UpdateUserResponse;
import models.setUser.User;
import models.setUser.CreateUserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.UsersSpec.*;


public class ApiTests extends BaseTest {

    private final String dateTimePattern = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z$";

    @Tag("API")
    @DisplayName("Получение списка пользователей")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Пользователь")
    @Story("Получение данных о пользователях")
    @Test
    void getListUsersTest() {
        int pageNumber = 2;
        UsersListResponse response = step("Выполнить запрос", () ->
                given(noBodyRequestSpec)
                        .param("page", pageNumber)

                        .when()
                        .get("/users")

                        .then()
                        .spec(successfulResponseSpec)
                        .extract().as(UsersListResponse.class));

        step("Проверить, что total > 0", () ->
                assertThat(response.getTotal()).isGreaterThan(0)
        );
        step("Проверить, что page соответствует запрошенному номеру", () ->
                assertThat(response.getPage()).isEqualTo(pageNumber)
        );
        step("Проверить, что возвращен непустой список пользователей", () ->
                assertThat(response.getData()).isNotEmpty()
        );
    }

    @Tag("API")
    @DisplayName("Получение данных о существующем пользователе")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Пользователь")
    @Story("Получение данных о пользователях")
    @Test
    void getSingleUserTest() {
        SingleUserResponse response = step("Выполнить запрос", () ->
                given(noBodyRequestSpec)

                        .when()
                        .get("/users/2")

                        .then()
                        .spec(successfulResponseSpec)
                        .extract().as(SingleUserResponse.class));

        step("Проверить data.id", () ->
                assertThat(response.getData().getId()).isEqualTo(2)
        );
        step("Проверить data.email", () ->
                assertThat(response.getData().getEmail()).isEqualTo("janet.weaver@reqres.in")
        );
        step("Проверить data.first_name", () ->
                assertThat(response.getData().getFirstName()).isEqualTo("Janet")
        );
        step("Проверить data.last_name", () ->
                assertThat(response.getData().getLastName()).isEqualTo("Weaver")
        );
        step("Проверить data.avatar", () ->
                assertThat(response.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg")
        );
        step("Проверить support.url", () ->
                assertThat(response.getSupport().getUrl())
                        .isEqualTo("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral")
        );
        step("Проверить support.text", () ->
                assertThat(response.getSupport().getText())
                        .isEqualTo("Tired of writing endless social media content? Let Content Caddy generate it for you.")
        );
    }

    @Tag("API")
    @DisplayName("Получение данных о несуществующем пользователе")
    @Severity(SeverityLevel.MINOR)
    @Feature("Пользователь")
    @Story("Получение данных о пользователях")
    @Test
    void userNotFoundTest() {
        SingleUserResponse response = step("Выполнить запрос", () ->
                given(noBodyRequestSpec)

                        .when()
                        .get("/users/23")

                        .then()
                        .spec(notFoundResponseSpec)
                        .extract().as(SingleUserResponse.class));

        step("Проверить, что в ответе отсутствуют данные", () -> {
            assertThat(response.getData()).isNull();
            assertThat(response.getSupport()).isNull();
        });
    }

    @Tag("API")
    @DisplayName("Создание пользователя")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Пользователь")
    @Story("Редактирование пользователей")
    @Test
    void createUserTest() {
        User request = new User();
        request.setName("morpheus");
        request.setJob("leader");

        CreateUserResponse response = step("Выполнить запрос", () ->
                given(withBodyRequestSpec)
                        .body(request)

                        .when()
                        .post("/users")

                        .then()
                        .spec(createdResponseSpec)
                        .extract().as(CreateUserResponse.class)
        );

        step("Проверить, что name в ответе равен name из запроса", () ->
                assertThat(response.getName()).isEqualTo(request.getName())
        );
        step("Проверить, что job в ответе равен job из запроса", () ->
                assertThat(response.getJob()).isEqualTo(request.getJob())
        );
        step("Проверить, что в ответе непустой id", () ->
                assertThat(response.getId()).isNotBlank()
        );
        step("Проверить, что в ответе createdAt соответствует паттерну " + dateTimePattern, () ->
                assertThat(response.getCreatedAt()).matches(dateTimePattern)
        );
    }

    @Tag("API")
    @DisplayName("Изменение пользователя")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Пользователь")
    @Story("Редактирование пользователей")
    @Test
    void updateUserTest() {
        User request = new User();
        request.setName("morpheus");
        request.setJob("leader");

        UpdateUserResponse response = step("Выполнить запрос", () ->
                given(withBodyRequestSpec)
                        .body(request)

                        .when()
                        .put("/users/2")

                        .then()
                        .spec(successfulResponseSpec)
                        .extract().as(UpdateUserResponse.class));

        step("Проверить, что name в ответе равен name из запроса", () ->
                assertThat(response.getName()).isEqualTo(request.getName())
        );
        step("Проверить, что job в ответе равен job из запроса", () ->
                assertThat(response.getJob()).isEqualTo(request.getJob())
        );
        step("Проверить, что в ответе отсутствует id", () ->
                assertThat(response.getId()).isNull()
        );
        step("Проверить, что в ответе updatedAt соответствует паттерну " + dateTimePattern, () ->
                assertThat(response.getUpdatedAt()).matches(dateTimePattern)
        );
    }

    @Tag("API")
    @DisplayName("Удаление пользователя")
    @Severity(SeverityLevel.NORMAL)
    @Feature("Пользователь")
    @Story("Редактирование пользователей")
    @Test
    void deleteUserTest() {
        step("Выполнить запрос и проверить, что получен 204 код без body", () ->
                given(noBodyRequestSpec)

                        .when()
                        .delete("/users/2")

                        .then()
                        .spec(noContentResponseSpec));
    }
}
