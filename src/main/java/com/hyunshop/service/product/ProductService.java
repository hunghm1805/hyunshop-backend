package com.hyunshop.service.product;


import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.common.data.PageRes;
import com.hyunshop.dto.product.ProductDto;
import com.hyunshop.dto.product.ProductResponse;
import com.hyunshop.entity.product.ProductEntity;

public interface ProductService {
    PageRes<ProductEntity> search(Integer page, Integer pageSize, String sortProperty, String sortOrder, String textSearch, String priceMin, String priceMax);

    ProductResponse save(CurrentAccount currentAccount, ProductDto productDto);

    ProductResponse update(CurrentAccount currentAccount, ProductDto productDto);

    ProductEntity delete(CurrentAccount currentAccount, Integer id);

    ProductEntity findProductById(Integer id);
}
