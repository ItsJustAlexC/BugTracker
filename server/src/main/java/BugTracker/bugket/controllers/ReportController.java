package BugTracker.bugket.controllers;

import BugTracker.bugket.domain.ReportsService;
import BugTracker.bugket.domain.Result;
import BugTracker.bugket.models.Report;
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
@RequestMapping("/api/reports")
public class ReportController {
    ReportsService service;

    @Autowired
    JwtConverter jwtConverter;

    public ReportController(ReportsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Report> getAll() {
        return service.getAll();
    }

    @GetMapping("/incomplete")
    public List<Report> getAllIncomplete() {
        return service.getAllIncomplete();
    }

    @GetMapping("/author")
    public ResponseEntity<List<Report>> getByUsername(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        String username = user.getUsername();
        return new ResponseEntity<>(service.getByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/voted")
    public List<Report> getByVote(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        String username = user.getUsername();
        return service.getByVote(username);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Report report, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserDetails user = jwtConverter.getUserFromToken(token);
        if(!Objects.equals(report.getAuthorUsername(), user.getUsername())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Result<Report> result = service.add(report);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @PathVariable boolean status) {
        Result<Void> result = service.updateStatus(id, status);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(result.getMessages(), HttpStatus.BAD_REQUEST);
        }
    }
}
