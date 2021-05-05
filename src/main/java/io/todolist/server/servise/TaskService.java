package io.todolist.server.servise;

import io.todolist.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final UserRepository userRepository;

    TaskService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    //Ajouter une task a une list (max 10 item)
    //Supprimer une task d'une list
    //update task
}
