package com.blogapp.blog.dto.response;

import com.blogapp.blog.enums.BlogStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Full detail view of a blog post")
public class BlogDetailResponse {

    @Schema(description = "Blog ID")
    private String id;

    @Schema(description = "Blog title")
    private String title;

    @Schema(description = "URL-friendly slug")
    private String slug;

    @Schema(description = "Short excerpt")
    private String excerpt;

    @Schema(description = "Sanitized HTML content")
    private String contentHtml;

    @Schema(description = "JSON content (if available)")
    private String contentJson;

    @Schema(description = "Featured image URL")
    private String featuredImageUrl;

    @Schema(description = "Author name")
    private String authorName;

    @Schema(description = "Author email")
    private String authorEmail;

    @Schema(description = "Blog status")
    private BlogStatus status;

    @Schema(description = "Submitted date")
    private LocalDateTime submittedAt;

    @Schema(description = "Published date")
    private LocalDateTime publishedAt;

    @Schema(description = "Rejection reason (if rejected)")
    private String rejectionReason;

    @Schema(description = "Tags")
    private List<String> tags;

    @Schema(description = "Total likes")
    private long likesCount;

    @Schema(description = "Total dislikes")
    private long dislikesCount;

    @Schema(description = "Total comments")
    private long commentsCount;

    @Schema(description = "Total views")
    private long viewsCount;

    @Schema(description = "Created date")
    private LocalDateTime createdAt;
}
