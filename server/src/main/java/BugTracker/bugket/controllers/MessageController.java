package BugTracker.bugket.controllers;

import BugTracker.bugket.domain.MessageService;
import BugTracker.bugket.domain.Result;
import BugTracker.bugket.models.Message;
import BugTracker.bugket.security.JwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    MessageService service;

    @Autowired
    JwtConverter jwtConverter;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/{reportId}")
    public List<Message> getMessagesById(@PathVariable int reportId) {
        return service.getMessagesById(reportId);
    }

    @PostMapping
    public ResponseEntity<?> postMessage(@RequestBody Message message, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        if(!Objects.equals(message.getAuthorUsername(), user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<Message> result = service.add(message);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId) {
        Result<Void> result = service.delete(messageId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }
}
