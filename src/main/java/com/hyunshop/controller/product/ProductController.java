package com.hyunshop.controller.product;

import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.controller.BaseController;
import com.hyunshop.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController extends BaseController {

    @PostMapping("/product")
    public ResponseEntity<?> save(@RequestBody ProductDto productDto) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (!currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
        }
        return new ResponseEntity<>(productService.save(currentAccount, productDto), HttpStatus.OK);
    }

    @PutMapping("/product")
    public ResponseEntity<?> update(@RequestBody ProductDto productDto) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (!currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
        }
        return new ResponseEntity<>(productService.update(currentAccount, productDto), HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (!currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission!");
        }
        productService.delete(currentAccount, id);
        return new ResponseEntity<>("Xóa sản phẩm thành công!", HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductId(@PathVariable Integer id) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (!currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No permission!");
        }
        return new ResponseEntity<>(productService.findProductById(id), HttpStatus.OK);
    }
}
