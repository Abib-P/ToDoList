package io.todolist.api.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDTOTest {

    @Autowired
    private JacksonTester<UserDTO> json;

    @Test
    void should_deserialize_task_object() throws IOException {
        String givenString = """
                        {
                                "email" : "email",
                                "lastname" : "last",
                                "firstname" : "first",
                                "birthdate" : "1999-02-01",
                                "password" : "pass"
                        }""";
        UserDTO userDTO = json.parseObject(givenString);

        assertThat(userDTO.getFirstname()).isEqualTo("first");
        assertThat(userDTO.getLastname()).isEqualTo("last");
        assertThat(userDTO.getEmail()).isEqualTo("email");
        assertThat(userDTO.getBirthdate()).isEqualTo(LocalDate.of(1999,2,1));
        assertThat(userDTO.getPassword()).isEqualTo("pass");
    }

    @Test
    void should_serialize_task_object() throws IOException {

        UserDTO userDTO = new UserDTO();
        userDTO.setFirstname("first");
        userDTO.setBirthdate(LocalDate.of(1999,2,1));
        userDTO.setEmail("email");
        userDTO.setLastname("last");
        userDTO.setPassword("pass");


        JsonContent<UserDTO> jsonContent = json.write(userDTO);

        assertThat(jsonContent).extractingJsonPathStringValue("$.email").isEqualTo("email");
        assertThat(jsonContent).extractingJsonPathStringValue("$.lastname").isEqualTo("last");
        assertThat(jsonContent).extractingJsonPathStringValue("$.firstname").isEqualTo("first");
        assertThat(jsonContent).extractingJsonPathStringValue("$.birthdate").isEqualTo("1999-02-01");
        assertThat(jsonContent).extractingJsonPathStringValue("$.password").isEqualTo("pass");
    }
}