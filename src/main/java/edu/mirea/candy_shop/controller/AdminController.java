package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.requests.AddProductRequest;
import edu.mirea.candy_shop.service.PictureService;
import edu.mirea.candy_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Log4j2
public class AdminController {
    private final ProductService productService;
    private final PictureService pictureService;

    @CrossOrigin(origins = "http://127.0.0.1:5500")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @SneakyThrows
    public void addProduct(@ModelAttribute AddProductRequest request) {
        pictureService.putPicture(request.productName(), request.image().getInputStream());
        productService.addProduct(request);
        log.info(request);
    }
}
