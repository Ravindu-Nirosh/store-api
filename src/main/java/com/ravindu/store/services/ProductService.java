package com.ravindu.store.services;

import com.ravindu.store.dtos.ProductDto;
import com.ravindu.store.entities.Product;
import com.ravindu.store.exceptions.CategoryNotFoundException;
import com.ravindu.store.exceptions.ProductNotFoundException;
import com.ravindu.store.mappers.ProductMapper;
import com.ravindu.store.repositories.CategoryRepository;
import com.ravindu.store.repositories.ProductRepository;
import io.jsonwebtoken.impl.security.EdwardsCurve;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private CategoryRepository categoryRepository;

    public List<ProductDto> getAllProduct(Byte categoryId){
        List<Product> products;
        if(categoryId != null){
            products = productRepository.findByCategoryId(categoryId);
        }else {
            products =productRepository.findAllProductWithCategory();
        }
        return products.stream().map(product -> productMapper.toDto(product)).toList();
    }

    public ProductDto createProduct(ProductDto productDto){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                ()-> new CategoryNotFoundException()
        );

        var productToEntity = productMapper.toEntity(productDto);
        productToEntity.setCategory(category);
        var savedProduct= productRepository.save(productToEntity);

        return productMapper.toDto(savedProduct);
    }

    public ProductDto updateProduct(Long id,ProductDto productDto){
        var product = productRepository.findById(id).orElse(null);
        var category =categoryRepository.findById(productDto.getCategoryId()).orElse(null);

        if (category==null){
            throw new CategoryNotFoundException();
        }
        if (product==null){
            throw new ProductNotFoundException();
        }


        productMapper.update(productDto,product);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    public void deleteProduct(Long id){
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        productRepository.delete(product);
    }

}
