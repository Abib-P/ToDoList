package io.todolist.api.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class TaskDTOTest {

    @Autowired
    private JacksonTester<TaskDTO> json;

    @Test
    void should_deserialize_task_object() throws IOException {
        String givenString = """
                        {
                                "name" : "taskName",
                                "content" : "content"
                        }""";
        TaskDTO taskDTO = json.parseObject(givenString);

        assertThat(taskDTO.getName()).isEqualTo("taskName");
        assertThat(taskDTO.getContent()).isEqualTo("content");
    }

    @Test
    void should_serialize_task_object() throws IOException {

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setContent("content");
        taskDTO.setName("name");

        JsonContent<TaskDTO> jsonContent = json.write(taskDTO);

        assertThat(jsonContent).extractingJsonPathStringValue("$.content").isEqualTo("content");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("name");
    }
}