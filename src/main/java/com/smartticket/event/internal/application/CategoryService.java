package com.smartticket.event.internal.application;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.smartticket.event.internal.domain.Category;
import com.smartticket.event.internal.exception.CategoryIsAlreadyFoundException;
import com.smartticket.event.internal.persistence.CategoryRepository;
import com.smartticket.event.internal.web.dtoCat.CategoryResponse;
import com.smartticket.event.internal.web.dtoCat.CreateCategoryRequest;
import com.smartticket.event.internal.web.dtoCat.GetAllCategoryResponse;
import com.smartticket.event.internal.web.dtoCat.UpdateCategoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Cloudinary cloudinary;

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse create(CreateCategoryRequest request)
    {
        validateImage(request.image());

        String imageUrl = uploadImage(request.image());

        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new CategoryIsAlreadyFoundException(request.name());
        }

        Category category=Category.builder()
                .name(request.name())
                .imageUrl(imageUrl)
                .active(true)
                .build();
        Category savedCategory=categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getImageUrl()
        );
    }
    private void validateImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new IllegalArgumentException(
                    "Category image cannot be empty"
            );
        }

        String contentType = image.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {

            throw new IllegalArgumentException(
                    "File must be an image"
            );
        }
    }
    private String uploadImage(MultipartFile image) {

        try {

            Map<?, ?> result = cloudinary
                    .uploader()
                    .upload(
                            image.getBytes(),
                            ObjectUtils.emptyMap()
                    );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to upload category image",
                    e
            );
        }
    }

    public GetAllCategoryResponse getAllActiveCategories()
    {
        List<Category> categories = categoryRepository.findAllByActiveTrue();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getImageUrl()
                ))
                .toList();

        return new GetAllCategoryResponse(categoryResponses);
    }
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(
            UUID id,
            UpdateCategoryRequest request
    ) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        category.setName(request.name());
        category.setImageUrl(uploadImage(request.image()));

        Category updatedCategory = categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    // Deactivate category
    @PreAuthorize("hasRole('ADMIN')")
    public void deactivate(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        category.setActive(false);

        categoryRepository.save(category);
    }

    // Activate category
    @PreAuthorize("hasRole('ADMIN')")
    public void activate(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found")
                );

        category.setActive(true);

        categoryRepository.save(category);
    }
    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getImageUrl()
        );
    }
}
