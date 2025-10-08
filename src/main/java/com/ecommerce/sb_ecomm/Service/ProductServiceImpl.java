package com.ecommerce.sb_ecomm.Service;

import com.ecommerce.sb_ecomm.Payload.ProductDTO;
import com.ecommerce.sb_ecomm.Payload.ProductResponse;
import com.ecommerce.sb_ecomm.exceptions.APIException;
import com.ecommerce.sb_ecomm.exceptions.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.model.Category;
import com.ecommerce.sb_ecomm.model.Product;
import com.ecommerce.sb_ecomm.repositories.CategoryRepository;
import com.ecommerce.sb_ecomm.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    public ProductRepository productRepository;
    public CategoryRepository categoryRepository;
    public ModelMapper modelMapper;
    public FileService fileService;
    @Value("${image-loc}")
    private String path;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ModelMapper modelMapper, FileService fileService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }


    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "Category", categoryId));
        boolean ifProductNotPresent = true;
        List<Product> listOfProducts = category.getProduct();
        for (Product product : listOfProducts) {
            if (product.getProductName().equals(productDTO.getProductName())) {
                ifProductNotPresent = false;
                break;
            }
        }
        if (ifProductNotPresent) {
            Product product =  modelMapper.map(productDTO, Product.class);
            product.setProductId(null);
            product.setProductImageUrl("default.jpg");
            product.setCategory(category);
            double specialPrice = (product.getDiscountPrice() * 0.01) * product.getProductPrice();
            product.setDiscountPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else
            throw new APIException("Product Not Found");
        }


    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        List<Product> products = productPage.getContent();
        if (products.isEmpty()){
            throw new APIException("No Product Found");
        }
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Optional<Category> byId = categoryRepository.findById(categoryId);
        Category category = byId.orElseThrow(() -> new ResourceNotFoundException("Category", "Category", categoryId));
        Sort sort = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageProducts = productRepository.findByCategoryOrderByProductPriceAsc(category, pageable);
        List<Product> productList = pageProducts.getContent();
        if (productList.isEmpty()){
            throw new ResourceNotFoundException("Product", "Product", categoryId);
        }
        List<ProductDTO> productDTOS = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;

    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
       Sort sort = sortOrder.equalsIgnoreCase("asc") ?Sort.by(sortBy).ascending():
               Sort.by(sortBy).descending();
       Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
       Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%", pageable);
       List<Product> productList = pageProducts.getContent();
       if (productList.isEmpty()){
           throw new APIException("No Product has been found having keyword ::::"+ keyword);
       }
       List<ProductDTO> productDTOS = productList.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
       ProductResponse productResponse = new ProductResponse();
       productResponse.setContent(productDTOS);
       productResponse.setContent(productDTOS);
       productResponse.setPageNumber(pageProducts.getNumber());
       productResponse.setPageSize(pageProducts.getSize());
       productResponse.setTotalElements(pageProducts.getTotalElements());
       productResponse.setTotalPages(pageProducts.getTotalPages());
       productResponse.setLastPage(pageProducts.isLast());
       return productResponse;
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long productId) {
        Product savedProduct = productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "Product", productId));
        Product product = modelMapper.map(productDTO, Product.class);
        savedProduct.setProductName(product.getProductName());
        savedProduct.setProductPrice(product.getProductPrice());
        savedProduct.setProductImageUrl(product.getProductImageUrl());
        savedProduct.setProductQuantity(product.getProductQuantity());
        savedProduct.setProductDescription(product.getProductDescription());
        savedProduct.setDiscountPrice(product.getDiscountPrice());
        double specialPrice = (savedProduct.getDiscountPrice() * 0.01) * savedProduct.getProductPrice();
        savedProduct.setDiscountPrice(specialPrice);
        productRepository.save(savedProduct);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product savedProduct = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "Product", productId));
        ProductDTO deletedProduct = modelMapper.map(savedProduct, ProductDTO.class);
        productRepository.delete(savedProduct);
        return deletedProduct;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB =  productRepository.findById(productId).
                orElseThrow(() -> new ResourceNotFoundException("Product", "Product", productId));
        String fileName = fileService.uploadImage(path, image);
        productFromDB.setProductImageUrl(fileName);
        productRepository.save(productFromDB);
        return modelMapper.map(productFromDB, ProductDTO.class);
    }

   }
