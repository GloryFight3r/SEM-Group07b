package pizzeria.order.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pizzeria.order.domain.store.StoreService;
import pizzeria.order.domain.store.Store;
import pizzeria.order.models.StoreModel;

import java.util.List;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    private final transient StoreService storeService;

    @PostMapping("/create")
    public ResponseEntity<Store> createStore(@RequestBody StoreModel store) {
        if (store.getLocation().isEmpty() || store.getContact().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arguments for store are invalid");
        }

        try {
            Store saved = storeService.addStore(store.parseToStore());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @PutMapping("/edit")
    public ResponseEntity editStore(@RequestBody StoreModel store) {
        if (store.getLocation().isEmpty() || store.getContact().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arguments for store are invalid");
        }

        try {
            boolean isEdited = storeService.editStore(store.getId(), store.parseToStore());
            if (isEdited) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteStore(@RequestBody Long storeId) {
        try {
            boolean flag = storeService.deleteStore(storeId);
            if (flag) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header(HttpHeaders.WARNING, e.getMessage()).build();
        }
    }

    @GetMapping("/get_stores")
    public List<Store> getStores() {
        try {
            return storeService.getAllStores();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
