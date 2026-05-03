package com.sweetcorner.incubyte_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sweetcorner.incubyte_backend.entity.CartItem;
import com.sweetcorner.incubyte_backend.service.GuestCartService;

@RestController
@RequestMapping("/api/guest/cart")
@CrossOrigin("*")
public class GuestCartController {

    @Autowired
    private GuestCartService guestCartService;

    //endpoint to add the item
    //React will call: POST http://localhost:8080/api/guest/cart/add?sessionId=abc-123

    @PostMapping("/add")
    public ResponseEntity<String> addItem(@RequestParam String sessionId,@RequestParam CartItem item){
        guestCartService.addItemToGuestCart(sessionId, item);
        return ResponseEntity.ok("Item added to giuest cart in the Redis!");
    }

    //endpoint to view the cart
    //React will call: GET http://localhost:8080/api/guest/cart/view?sessionId=abc-123

    @GetMapping("/view")
    public ResponseEntity<Lis
}
