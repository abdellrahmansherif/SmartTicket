package com.smartticket.event.internal.web.dtoCat;

import java.util.List;

public record GetAllCategoryResponse(
        List<CategoryResponse>categoryResponses
) {
}
