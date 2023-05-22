package BugTracker.bugket.controllers;

import BugTracker.bugket.domain.Result;
import BugTracker.bugket.domain.VoteService;
import BugTracker.bugket.security.JwtConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    VoteService service;

    @Autowired
    JwtConverter jwtConverter;

    public VoteController(VoteService service) {
        this.service = service;
    }

    @GetMapping("/check/{reportId}")
    public ResponseEntity<Boolean> checkVoters(@PathVariable int reportId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        String username = user.getUsername();

        boolean result = service.checkVoters(username, reportId);
        return new ResponseEntity<Boolean>(result, HttpStatus.OK);
    }

    @PostMapping("/{reportId}")
    public ResponseEntity<?> addVote(@PathVariable int reportId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        String username = user.getUsername();

        Result<Void> result = service.addVote(username, reportId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteVote(@PathVariable int reportId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        String username = user.getUsername();

        Result<Void> result = service.deleteVote(username, reportId);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }
}
