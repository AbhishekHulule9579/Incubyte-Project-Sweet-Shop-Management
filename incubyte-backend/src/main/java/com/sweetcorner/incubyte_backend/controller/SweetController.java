package com.sweetcorner.incubyte_backend.controller;

import com.sweetcorner.incubyte_backend.dto.SweetRequest;
import com.sweetcorner.incubyte_backend.entity.Sweet;
import com.sweetcorner.incubyte_backend.service.SweetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sweets")
@CrossOrigin(origins = "http://localhost:5173")
public class SweetController {

    private final SweetService sweetService;

    public SweetController(SweetService sweetService) {
        this.sweetService = sweetService;
    }

    @GetMapping
    public List<Sweet> getAllSweets() {
        return sweetService.getAllSweets();
    }

    @GetMapping("/category/{name}")
    public List<Sweet> getSweetsByCategory(@PathVariable String name) {
        return sweetService.getSweetsByCategory(name);
    }

    @PostMapping
    public ResponseEntity<?> addSweet(@RequestBody SweetRequest request) {
        try {
            return ResponseEntity.ok(sweetService.addSweet(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSweet(@PathVariable Long id, @RequestBody SweetRequest request) {
        try {
            return ResponseEntity.ok(sweetService.updateSweet(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{id}/restock")
    public ResponseEntity<?> restockSweet(@PathVariable Long id, @RequestBody Map<String, Integer> payload) {
        try {
            Integer quantity = payload.get("quantity");
            if (quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid quantity"));
            }
            return ResponseEntity.ok(sweetService.restockSweet(id, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSweet(@PathVariable Long id) {
        try {
            sweetService.deleteSweet(id);
            return ResponseEntity.ok(Map.of("message", "Sweet deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseSweets(@RequestBody List<Map<String, Object>> items,
            java.security.Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(401).body(Map.of("message", "User not authenticated"));
            }
            sweetService.purchaseSweets(items, principal.getName());
            return ResponseEntity.ok(Map.of("message", "Purchase successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getMyOrders(java.security.Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(401).body(Map.of("message", "User not authenticated"));
            }
            return ResponseEntity.ok(sweetService.getUserOrders(principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "File is empty"));
            }

            // Create uploads dir if not exists
            java.nio.file.Path uploadPath = java.nio.file.Paths.get("uploads");
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                originalFilename = "image";
            }
            String sanitizedFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String filename = java.util.UUID.randomUUID().toString() + "_" + sanitizedFilename;
            java.nio.file.Path filePath = uploadPath.resolve(filename);

            // Save file
            java.nio.file.Files.copy(file.getInputStream(), filePath);

            // Return URL
            String fileUrl = "http://localhost:8080/uploads/" + filename;
            return ResponseEntity.ok(Map.of("url", fileUrl));

        } catch (java.io.IOException e) {
            return ResponseEntity.status(500).body(Map.of("message", "Failed to upload file: " + e.getMessage()));
        }
    }
}
