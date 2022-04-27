package com.hyunshop.controller.order;

import com.hyunshop.common.constances.RoleEnums;
import com.hyunshop.common.constances.StatusEnums;
import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.controller.BaseController;
import com.hyunshop.dto.order.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class OrderController extends BaseController {

    @GetMapping("/order/search")
    public ResponseEntity<?> search(@RequestParam() int page,
                                    @RequestParam() int pageSize,
                                    @RequestParam(required = false, defaultValue = "") String sortProperty,
                                    @RequestParam(required = false, defaultValue = "desc") String sortOrder,
                                    @RequestParam(required = false) String textSearch,
                                    @RequestParam(required = false) String dateType) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            return new ResponseEntity<>(orderService.search(page, pageSize, sortProperty, sortOrder, textSearch, dateType), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
    }

    @PostMapping("/order")
    public ResponseEntity<?> save(@RequestBody OrderDto orderDto) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (currentAccount.getRoles().contains(RoleEnums.ROLE_CUSTOMER.name())) {
            return new ResponseEntity<>(orderService.save(currentAccount, orderDto), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
    }

    @PutMapping("/order")
    public ResponseEntity<?> update(@RequestBody OrderDto orderDto) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            return new ResponseEntity<>(orderService.update(currentAccount, orderDto), HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
    }

    @GetMapping("/order/update/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestParam() StatusEnums status) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            orderService.updateStatus(currentAccount, id, status);
            return ResponseEntity.ok("Cập nhật thành công!");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
    }

    @GetMapping("/order/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        CurrentAccount currentAccount = getCurrentAccount();
        if (currentAccount.getRoles().contains(RoleEnums.ROLE_ADMIN.name())) {
            orderService.delete(currentAccount, id);
            return ResponseEntity.ok("Xóa thành công!");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No permission!");
    }
}
