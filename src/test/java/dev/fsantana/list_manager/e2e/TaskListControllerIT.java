package dev.fsantana.list_manager.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import dev.fsantana.list_manager.api.dto.input.InputTaskList;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.repository.TaskListRepository;
import io.restassured.RestAssured;

import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskListControllerIT {

    @Autowired
    private TaskListRepository repository;

    @BeforeAll
    public static void config() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = 8080;
        RestAssured.basePath = "/task-lists";
    }
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public  void cleanDataBase(){
        repository.deleteAll();
    }

    @Test
    @DisplayName("should return status code 200 with  values")
    public void test0() {
        repository.saveAndFlush(new TaskList("Felipe"));
        when().get()
                .then()
                .statusCode(200)
                .body("totalElements", is(1)).log().all();
    }

    @Test
    @DisplayName("should return status 200 when find by id")
    public void test1() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Felipe"));
        when().get("/{id}", taskList.getId())
                .then()
                .statusCode(200)
                .body("id", is(taskList.getId().intValue()))
                .body("title", is(taskList.getTitle()))
                .log().all();
    }

    @Test
    @DisplayName("should return status 404 when find by id")
    public void test2() throws JsonProcessingException {
        when().get("/{id}", 1)
                .then()
                .statusCode(404)
                .body("detail", is("Lista de tarefas não existes ou não foi encontrada"))
                .body("title", is("Recurso não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 400 when save without a title")
    public void test3() throws JsonProcessingException {
        given().body(InputTaskList.builder().build())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("detail", is("Um ou mais campos estão invalidos. Faça o preenchimento correto e tente novamente"))
                .body("title", is("Dados Invalidos"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 400 when save with a title wit 160 characters")
    public void test4() throws JsonProcessingException {
        given().body(
                InputTaskList.builder()
                        .title("21654asda894as654as da5d4as89 da35sa4das 98asdas 3asda4sd84ad 3a54asd 98asdad65a4 3asd5a4sd 98a4dadddddddddddddddddddddddd aaaaaaaaaaaaaaaaaaaa ddddddddddddddda")
                        .build())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("detail", is("Um ou mais campos estão invalidos. Faça o preenchimento correto e tente novamente"))
                .body("title", is("Dados Invalidos"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 404 when save when title alread exits")
    public void test5() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Task 1"));
        given().body(InputTaskList.builder().title("Task 1").build())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(400)
                .body("detail", is("Já existe uma lista de tarefa de tarefas cadastradas"))
                .body("title", is("Violação Regra de Negocio"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 201 when save when title alread exits")
    public void test6() throws JsonProcessingException {
        given().body(InputTaskList.builder().title("Task 1").build())
                .contentType(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("title", is("Task 1"))
                .body("createdAt", notNullValue())
                .log().all();
    }

    @Test
    @DisplayName("should return status 404 when put and task list not exits")
    public void test7() throws JsonProcessingException {
        given().body(InputTaskList.builder().title("Title").build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}", 1L)
                .then()
                .statusCode(404)
                .body("detail", is("Lista de tarefas não existes ou não foi encontrada"))
                .body("title", is("Recurso não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 400 when put without a title")
    public void test8() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Task 1"));
        given().body(InputTaskList.builder().build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}", taskList.getId())
                .then()
                .statusCode(400)
                .body("detail", is("Um ou mais campos estão invalidos. Faça o preenchimento correto e tente novamente"))
                .body("title", is("Dados Invalidos"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 400 when put with a title wit 160 characters")
    public void test9() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Task 1"));
        given().body(
                        InputTaskList.builder()
                                .title("21654asda894as654as da5d4as89 da35sa4das 98asdas 3asda4sd84ad 3a54asd 98asdad65a4 3asd5a4sd 98a4dadddddddddddddddddddddddd aaaaaaaaaaaaaaaaaaaa ddddddddddddddda")
                                .build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}", taskList.getId())
                .then()
                .statusCode(400)
                .body("detail", is("Um ou mais campos estão invalidos. Faça o preenchimento correto e tente novamente"))
                .body("title", is("Dados Invalidos"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 400 when put with a exited title")
    public void test10() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Task 1"));
        given().body(InputTaskList.builder().title("Task 1").build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}", taskList.getId())
                .then()
                .statusCode(400)
                .body("detail", is("Já existe uma lista de tarefa de tarefas cadastradas"))
                .body("title", is("Violação Regra de Negocio"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 200 when put with valid title")
    public void test11() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Task 0"));
        given().body(InputTaskList.builder().title("Task 1").build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{id}", taskList.getId())
                .then()
                .statusCode(200)
                .body("id", is(taskList.getId().intValue()))
                .body("title", is("Task 1"))
                .body("createdAt", notNullValue())
                .log().all();
    }


    @Test
    @DisplayName("should return status 404 when delete and task list not exits")
    public void test12() throws JsonProcessingException {
                when()
                .delete("/{id}", 1L)
                .then()
                        .statusCode(404)
                        .body("detail", is("Lista de tarefas não existes ou não foi encontrada"))
                        .body("title", is("Recurso não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 204 when delete and task list not exits")
    public void test13() throws JsonProcessingException {
        TaskList taskList = repository.saveAndFlush(new TaskList("Task 0"));
        when()
                .delete("/{id}", taskList.getId())
                .then()
                .statusCode(204)
                .log().all();
    }
}
