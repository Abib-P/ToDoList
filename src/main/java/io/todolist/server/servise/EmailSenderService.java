package io.todolist.server.servise;

import io.todolist.server.user.User;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderService {

    EmailSenderService() {

    }

    public void sendEmailTo(User user) {
        throw new UnsupportedOperationException();
    }
}
