package com.simplews404.Simple.WS.CMP404;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.azure.cosmos.models.PartitionKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="{username}")
    public ResponseEntity<User> getUser(@PathVariable("username") String username) {
        Optional<User> user = userRepository.findById(username, new PartitionKey(username));
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> result = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(result::add);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User saved = userRepository.save(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping(path="{username}")
    public ResponseEntity<User> replaceUser(@RequestBody User user) {
        Optional<User> existingUser = userRepository.findById(user.getUsername(), new PartitionKey(user.getUsername()));
        if (!existingUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User saved = userRepository.save(user);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable("username") String username) {
        userRepository.deleteById(username, new PartitionKey(username));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
