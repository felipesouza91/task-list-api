package dev.fsantana.list_manager.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import dev.fsantana.list_manager.domain.model.TaskList;
import dev.fsantana.list_manager.domain.service.TaskListService;

@WebMvcTest(TaskListResource.class)
class TaskListResourceTest {

    private final String PATH = "/task-lists";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskListService clientService;

    @Test
    @DisplayName("should return status code 200 with tasklist when exits")
    public void test0() throws Exception {
        TaskList taskList = new TaskList(1L, "Task List 1", OffsetDateTime.now());
        given(clientService.findById(1L)).willReturn(taskList);
        String formatedDate = JsonPath.parse(objectMapper.writeValueAsString(taskList.getCreatedAt())).json();
        mockMvc.perform(get(PATH + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskList.getId().intValue())))
                .andExpect(jsonPath("$.title", is(taskList.getTitle())))
                .andExpect(jsonPath("$.createdAt", is(formatedDate)));
    }

}