package dev.fsantana.list_manager.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.fsantana.list_manager.api.dto.input.InputTaskItem;
import dev.fsantana.list_manager.api.dto.input.InputTaskList;
import dev.fsantana.list_manager.domain.model.TaskItem;
import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.repository.TaskItemRepository;
import dev.fsantana.list_manager.domain.repository.TaskListRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TaskListItemResourceIT {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskItemRepository taskItemRepository;

    @BeforeAll
    public static void config() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.port = 8080;
        RestAssured.basePath = "/task-lists";
    }

    @AfterEach
    public  void cleanDataBase(){
        taskListRepository.deleteAll();
        taskItemRepository.deleteAll();
    }

    @Test
    @DisplayName("should return status 204 when delete a task Item ")
    public void test0() throws JsonProcessingException {
        TaskList taskList = taskListRepository.saveAndFlush(new TaskList("Task 0"));
        TaskItem taskitem = taskItemRepository.save(new TaskItem(null, "Item 1", "Item Exemplae", true, false, OffsetDateTime.now(), taskList));
        when()
                .delete("/{taskId}/items/{itemId}", taskList.getId(), taskitem.getId())
                .then()
                .statusCode(204)
                .log().all();
    }

    @Test
    @DisplayName("should return status 400 when delete task item delete and task item not exits")
    public void test1() throws JsonProcessingException {
        when()
                .delete("/{taskId}/items/{itemId}", 1L, 1L)
                .then()
                .statusCode(400)
                .body("detail", is("Lista de tarefas não existes ou não foi encontrada"))
                .body("title", is("Violação Regra de Negocio"))
                .log().all();
    }

    @Test
    @DisplayName("should return status 404 when delete task item and task list not exits")
    public void test2() throws JsonProcessingException {
        TaskList taskList = taskListRepository.saveAndFlush(new TaskList("Task 0"));
        when()
                .delete("/{taskId}/items/{itemId}", taskList.getId(), 1L)
                .then()
                .statusCode(404)
                .body("detail", is("O item não existe ou não foi encontrado"))
                .body("title", is("Recurso não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("should return 400 when update and pass a invalid taskList id")
    public void test3() {
        given().body(InputTaskItem.builder().title("Item Task 1").description("Some description").build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{taskId}/items/{itemId}",  1L, 1L)
                .then()
                .statusCode(400)
                .body("detail", is("Lista de tarefas não existes ou não foi encontrada"))
                .body("title", is("Violação Regra de Negocio"))
                .log().all();
    }

    @Test
    @DisplayName("should return 400 when update and pass a invalid taskitem id")
    public void test4() {
        TaskList taskList = taskListRepository.saveAndFlush(new TaskList("Task 0"));
        given().body(InputTaskItem.builder().title("Item Task 1").description("Some description").build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{taskId}/items/{itemId}",  taskList.getId(), 1L)
                .then()
                .statusCode(404)
                .body("detail", is("O item não existe ou não foi encontrado"))
                .body("title", is("Recurso não encontrado"))
                .log().all();
    }

    @Test
    @DisplayName("should return 400 when update and validation fails")
    public void test5() {
        TaskList taskList = taskListRepository.saveAndFlush(new TaskList("Task 0"));
        given().body(InputTaskItem.builder().build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{taskId}/items/{itemId}",  taskList.getId(), 1L)
                .then()
                .statusCode(400)
                .body("detail", is("Um ou mais campos estão invalidos. Faça o preenchimento correto e tente novamente"))
                .body("title", is("Dados Invalidos"))
                .log().all();
    }

    @Test
    @DisplayName("should return 400 when update task item of different list")
    public void test6() {
        TaskList taskList = taskListRepository.saveAndFlush(new TaskList("Task 0"));
        TaskList taskList2 = taskListRepository.saveAndFlush(new TaskList("Task 1"));
        TaskItem taskitem = taskItemRepository.save(new TaskItem(null, "Item 1", "Item Exemplae", true, false, OffsetDateTime.now(), taskList));
        given().body(InputTaskItem.builder().title("Item 1 - new").build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{taskId}/items/{itemId}",  taskList2.getId(), taskitem.getId())
                .then()
                .statusCode(400)
                .body("detail", is("Não e possivel trocar a tarefa de lista"))
                .body("title", is("Violação Regra de Negocio"))
                .log().all();
    }

    @Test
    @DisplayName("should return 200 when update task item success")
    public void test7() {
        TaskList taskList = taskListRepository.saveAndFlush(new TaskList("Task 0"));
        TaskItem taskitem = taskItemRepository.save(new TaskItem(null, "Item 1", "Item example", true, false, OffsetDateTime.now(), taskList));
        given().body(InputTaskItem.builder().title("Item 1 - new").description(taskitem.getDescription()).isActive(false).isPriority(true).build())
                .contentType(ContentType.JSON)
                .when()
                .put("/{taskId}/items/{itemId}",  taskList.getId(), taskitem.getId())
                .then()
                .statusCode(200)
                .body("id", is(taskitem.getId().intValue()))
                .body("title", is("Item 1 - new"))
                .body("description", is(taskitem.getDescription()))
                .body("isActive", is(Boolean.FALSE))
                .body("isPriority", is(Boolean.TRUE))

                .log().all();
    }
}
