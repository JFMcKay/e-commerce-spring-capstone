package com.revature.controllers;

import com.revature.annotations.Authorized;
import com.revature.dtos.ProductReviewRequest;
import com.revature.dtos.ProductReviewResponse;
import com.revature.models.ProductReview;
import com.revature.models.User;
import com.revature.services.ProductReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/productreview")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"}, allowCredentials = "true")
public class ProductReviewController {

    private final ProductReviewService productReviewService;


    public ProductReviewController(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;
    }

    @Authorized
    @GetMapping
    public ResponseEntity<List<ProductReviewResponse>> getReviews() {
        return ResponseEntity.ok(productReviewService.findAll());
    }

    @Authorized
    @GetMapping("/{id}")
    public ResponseEntity<List<ProductReviewResponse>> getReviewsByProductId(@PathVariable("id") int id) {
        return ResponseEntity.ok(productReviewService.findByProductId(id));

    }

    @Authorized
    @PutMapping
    public ResponseEntity<ProductReviewResponse> upsert(@RequestBody ProductReviewRequest productReview, HttpSession session) {
        if(session.getAttribute("user") == null) {
            return ResponseEntity.notFound().build();
        }
        User user = (User)session.getAttribute("user");
        return ResponseEntity.ok(new ProductReviewResponse(productReviewService.save(productReview, user)));
    }

    @Authorized
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id, HttpSession session) {
        if(session.getAttribute("user") == null) {
            return;
        }
        productReviewService.deleteById(id);
    }
}