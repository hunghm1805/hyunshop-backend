package com.hyunshop.controller.noAuth;

import com.hyunshop.controller.BaseController;
import com.hyunshop.dto.noAuth.RegisterDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/no-auth")
public class NoAuthController extends BaseController {

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        noAuthService.register(registerDto);
        return ResponseEntity.ok().body("Đăng ký tài khoản thành công!");
    }

    @GetMapping("/product/search")
    public ResponseEntity<?> searchProduct(
            @RequestParam() int page,
            @RequestParam() int pageSize,
            @RequestParam(required = false, defaultValue = "name") String sortProperty,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder,
            @RequestParam(required = false) String textSearch,
            @RequestParam(required = false) String priceMin,
            @RequestParam(required = false) String priceMax
    ) {
        return new ResponseEntity<>(productService.search(page, pageSize, sortProperty, sortOrder, textSearch, priceMin, priceMax), HttpStatus.OK);
    }
}
