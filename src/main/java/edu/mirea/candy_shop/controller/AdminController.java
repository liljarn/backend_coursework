package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.requests.AddNewProductRequest;
import edu.mirea.candy_shop.service.PictureService;
import edu.mirea.candy_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Log4j2
public class AdminController {
    private final ProductService productService;
    private final PictureService pictureService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @SneakyThrows
    public void addProduct(@ModelAttribute AddNewProductRequest request) {
        pictureService.putProductPicture(request.productName(), request.image().getInputStream());
        productService.addProduct(request);
    }
}
