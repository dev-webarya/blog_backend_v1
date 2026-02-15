package com.blogapp.admin.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Approve a blog post")
public class ApproveBlogRequest {

    @Schema(description = "Admin user ID performing the approval", example = "admin-001")
    private String adminId;
}
