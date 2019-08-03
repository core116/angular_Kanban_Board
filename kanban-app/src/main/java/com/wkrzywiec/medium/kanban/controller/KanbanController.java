package com.wkrzywiec.medium.kanban.controller;

import com.wkrzywiec.medium.kanban.model.Kanban;
import com.wkrzywiec.medium.kanban.repository.KanbanRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kanbans")
@RequiredArgsConstructor
public class KanbanController {

    private final KanbanRepository kanbanRepository;

    @GetMapping("/")
    @ApiOperation(value="View a list of all Kanban boards", response = Kanban.class, responseContainer = "List")
    public ResponseEntity<?> getAllKanbans(){
        try {
            List<Kanban> kanbanList = new ArrayList<>();
            kanbanRepository.findAll().forEach(kanbanList::add);
            return new ResponseEntity<>(kanbanList, HttpStatus.OK);
        } catch (RuntimeException e) {
            return returnError();
        }

    }

    @GetMapping("/{id}")
    @ApiOperation(value="Find a Kanban board info by its id", response = Kanban.class)
    public ResponseEntity<?> getKanban(@PathVariable Long id){
        try {
            Optional<Kanban> optKanban = kanbanRepository.findById(id);
            if (optKanban.isPresent()) {
                return new ResponseEntity<>(optKanban.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return returnError();
        }
    }

    @PostMapping("/")
    @ApiOperation(value="Save new Kanban board", response = Kanban.class)
    public ResponseEntity<?> createKanban(@RequestBody Kanban kanban){
        try {
            return new ResponseEntity<>(kanbanRepository.save(kanban), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return returnError();
        }

    }

    @PutMapping("/")
    public ResponseEntity<?> updateKanban(@RequestBody Kanban kanban){
        return null;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="Delete Kanban board with specific id", response = String.class)
    public ResponseEntity<?> deleteKanban(@PathVariable Long id){
        try {
            Optional<Kanban> optKanban = kanbanRepository.findById(id);
            if (optKanban.isPresent()) {
                kanbanRepository.delete(optKanban.get());
                return new ResponseEntity<>(String.format("Kanban with id: %d was deleted", id), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No kanban found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return returnError();
        }
    }

    @GetMapping("/{kanbanId}/tasks/")
    public ResponseEntity<?> getAllTasksInKanban(@PathVariable Long kanbanId){
        return null;
    }

    private ResponseEntity<String> returnError(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
