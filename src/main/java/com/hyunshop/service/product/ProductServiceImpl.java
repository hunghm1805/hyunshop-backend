package com.hyunshop.service.product;

import com.hyunshop.common.data.CurrentAccount;
import com.hyunshop.common.data.PageReq;
import com.hyunshop.common.data.PageRes;
import com.hyunshop.common.data.Pages;
import com.hyunshop.common.util.PaginationUtils;
import com.hyunshop.common.util.StringUtils;
import com.hyunshop.common.util.ValidateUtils;
import com.hyunshop.dto.product.ProductDto;
import com.hyunshop.dto.product.ProductResponse;
import com.hyunshop.entity.category.CategoryEntity;
import com.hyunshop.entity.product.ProductEntity;
import com.hyunshop.repository.category.CategoryRepository;
import com.hyunshop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final  CategoryRepository categoryRepository;

    @Override
    public PageRes<ProductEntity> search(Integer page, Integer pageSize, String sortProperty, String sortOrder, String textSearch, String priceMin, String priceMax) {
        PageReq pageReq = new PageReq(page, pageSize, sortProperty, sortOrder);
        Pageable pageable = PaginationUtils.getPageable(pageReq);
        Page<ProductEntity> productEntities = null;

        //TH request gui len null ca textSearch va khoang gia
        if (textSearch == null
                && priceMin == null
                && priceMax == null) {
            productEntities = productRepository.findAll(pageable); //tim tat ca

            //TH request gui len khong null gia tri nao
        } else if (textSearch != null
                && priceMin != null && StringUtils.isNumeric(priceMin)
                && priceMax != null && StringUtils.isNumeric(priceMax)) {
            long min = Long.valueOf(priceMin);
            long max = Long.valueOf(priceMax);
            try {
                productEntities = productRepository.findAllByNameAndByMinAndByMax(textSearch.toLowerCase(), min, max, pageable); //tim theo textsearch va khoang gia
            } catch (NumberFormatException e) {
                productEntities = productRepository.findAll(pageable);
                e.printStackTrace();
            }
            //TH request gui len null khoang gia, textsearch khong null thi tim tat ca dich vu theo ten (textsearch) truyen len
        } else if (textSearch != null
                && priceMin == null
                && priceMax == null) {
            productEntities = productRepository.findByNameIgnoreCase(textSearch.toLowerCase().trim(), pageable);
        } else if (textSearch == null //TH request null textsearch, khoang gia khong null thi tim tat ca dich vu trong khoang gia min & max
                && priceMin != null
                && priceMax != null
                && StringUtils.isNumeric(priceMin.trim())
                && StringUtils.isNumeric(priceMax.trim())) {
            try {
                Long min = Long.valueOf(priceMin);
                Long max = Long.valueOf(priceMax);
                productEntities = productRepository.findAllByPriceMinAndPriceMax(min, max, pageable);
            } catch (NumberFormatException e) {
                productEntities = productRepository.findAll(pageable);
                e.printStackTrace();
            }
        }

        if (productEntities != null) {
            List<ProductEntity> productsEntityList = productEntities.getContent();
            Pages pages = getPages((int) productEntities.getTotalElements(), pageReq);
            boolean hasNext = pageReq.getPage() < pages.getTotalPages();
            boolean hasPrevious = pageReq.getPage() > 1;
            return new PageRes<>(productsEntityList, pages.getTotalPages(), pages.getTotalElements(), hasNext, hasPrevious);
        }
        return new PageRes<>(new ArrayList<>(), 0, 0, false, false);
    }

    @Override
    public ProductResponse save(CurrentAccount currentAccount, ProductDto productDto) {
        validateProductDto(productDto);
        if (productRepository.existsByNameIgnoreCase(productDto.getName().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sản phẩm đã tồn tại!");
        }
        ProductEntity product = null;
        CategoryEntity category = categoryRepository.findById(Integer.parseInt(productDto.getCategoryId())).orElse(null);

        if (category != null) {
            product = ProductEntity.builder()
                    .name(productDto.getName())
                    .image(productDto.getImage())
                    .price(Double.parseDouble(productDto.getPrice()))
                    .available(true)
                    .categoryId(Integer.parseInt(productDto.getCategoryId()))
                    .categoryEntity(category)
                    .build();
            product.setCreatedBy(currentAccount.getUsername());
            product.setCreatedTime(new Date().getTime());
            productRepository.save(product);
        }

        return new ProductResponse(product);
    }

    @Override
    public ProductResponse update(CurrentAccount currentAccount, ProductDto productDto) {
        validateProductDto(productDto);
        ProductEntity productEntity = productRepository.findByIdAndAvailableIsTrue(productDto.getId()).orElseThrow((() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm có id: " + productDto.getId() + "!");
        }));
        if (productEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy sản phẩm!");
        }

        if (productRepository.existsByNameIgnoreCaseAndIdNotLike(productDto.getName().trim(), productDto.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên sản phẩm đã tồn tại!");
        }

        CategoryEntity categoryEntity = categoryRepository.findById(Integer.parseInt(productDto.getCategoryId())).orElse(null);
            productEntity.setId(productDto.getId());
            productEntity.setName(productDto.getName());
            productEntity.setPrice(Double.parseDouble(productDto.getPrice()));
            productEntity.setImage(productDto.getImage());
            productEntity.setCategoryId(categoryEntity.getId());
            productEntity.setCategoryEntity(categoryEntity);
            productEntity.setUpdatedBy(currentAccount.getUsername());
            productEntity.setUpdatedTime(new Date().getTime());
            productRepository.save(productEntity);
        return new ProductResponse(productEntity);
    }

    @Override
    public ProductEntity delete(CurrentAccount currentAccount, Integer id) {
        ProductEntity product = productRepository.findByIdAndAvailableIsTrue(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bệnh nhân có id: " + id + "!");
        });
        product.setUpdatedBy(currentAccount.getUsername());
        product.setUpdatedTime(new Date().getTime());
        product.setAvailable(Boolean.FALSE);
        productRepository.save(product);
        return product;
    }

    @Override
    public ProductEntity findProductById(Integer id) {
        return productRepository.findByIdAndAvailableIsTrue(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không tìm thấy sản phẩm có id: " + id + "!");
        });
    }

    private void validateProductDto(ProductDto productDto) {
        HashMap<String, String> map = new HashMap<>();
        map.put("tên", productDto.getName());
        map.put("hình ảnh", productDto.getImage());
        map.put("mã loại sản phẩm", productDto.getCategoryId());
        ValidateUtils.validateNullOrBlankString(map);
        ValidateUtils.validateNullOrNegativeNumber(productDto.getPrice(), "Giá");

    }

    private Pages getPages(int listSize, PageReq pageReq) {
        Long totalElements = Long.valueOf(listSize);
        int totalPages = 0;
        if (totalElements > 0) {
            totalPages = (int) (totalElements % pageReq.getPageSize() == 0 ?
                    totalElements / pageReq.getPageSize() :
                    totalElements / pageReq.getPageSize() + 1);
        }
        Pages pages = new Pages(totalElements, totalPages, pageReq.getPageSize(), pageReq.getPage());
        return pages;
    }
}
