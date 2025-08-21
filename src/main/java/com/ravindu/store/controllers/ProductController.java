package com.ravindu.store.controllers;

import com.ravindu.store.dtos.ErrorDto;
import com.ravindu.store.dtos.ProductDto;
import com.ravindu.store.entities.Product;
import com.ravindu.store.exceptions.CategoryNotFoundException;
import com.ravindu.store.exceptions.ProductNotFoundException;
import com.ravindu.store.mappers.ProductMapper;
import com.ravindu.store.repositories.CategoryRepository;
import com.ravindu.store.repositories.ProductRepository;
import com.ravindu.store.services.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ProductService productService;


    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(required = false) Byte categoryId){
        return productService.getAllProduct(categoryId);
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder){
        var product =productService.createProduct(productDto);
        var uri=uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,@RequestBody ProductDto productDto){
        return ResponseEntity.ok(productService.updateProduct(id,productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCategoryNotFound() {
        // ex.getMessage() contains "Category Must be Provided" or whatever I passed
        return ResponseEntity.badRequest().body(new ErrorDto("invalid category id"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("Product not found"));
    }
}
