package com.examserver.controller;


import com.examserver.config.ResponseHandler;
import com.examserver.entity.Category;
import com.examserver.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/category")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<?> validateToken(String jwtToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

//        return restTemplate.exchange(
//                "http://localhost:8080/validate",
//                HttpMethod.GET,
//                entity,
//                Void.class
//        );
        return restTemplate.exchange(
                "https://usermanagementserver-j4xg.onrender.com/validate",
                HttpMethod.GET,
                entity,
                Void.class
        );
    }

    //addCategory
    @PostMapping("/")
    public ResponseEntity<?> addCategory(@RequestHeader("Authorization")  String jwtToken, @RequestBody Category category)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            Category category1 = this.categoryService.addCategory(category);
            return ResponseEntity.ok(category1);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //getCategory
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategory(@RequestHeader("Authorization")  String jwtToken,@PathVariable("categoryId") Long categoryId)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.categoryService.getCategory(categoryId));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //getAllCategories
    @GetMapping("/")
    public ResponseEntity<?> getCategories(@RequestHeader("Authorization")  String jwtToken)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.categoryService.getCategories());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //update category
    @PutMapping("/")
    public ResponseEntity<?> updateCategory(@RequestHeader("Authorization")  String jwtToken,@RequestBody Category category)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.ok(this.categoryService.updateCategory(category));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
    }

    //delete category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@RequestHeader("Authorization") String jwtToken,@PathVariable("categoryId") Long categoryId)
    {
        ResponseEntity<?> forEntity = validateToken(jwtToken);
        if(forEntity.getStatusCode().equals(HttpStatus.OK)) {
            boolean flag = this.categoryService.deleteCategory(categoryId);
            if(flag){
             //   return ResponseEntity.ok("Category Deleted Successfully");
                return ResponseHandler.generateResponse("Category deleted successfully",HttpStatus.OK);
            }
            else
                return ResponseHandler.generateResponse("Id Not Exists!!",HttpStatus.NOT_FOUND);
               // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id Not Exists");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Check");
        }

}
