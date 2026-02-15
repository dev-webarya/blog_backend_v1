package com.blogapp.blog.controller;

import com.blogapp.blog.dto.response.ArchiveResponse;
import com.blogapp.blog.dto.response.BlogDetailResponse;
import com.blogapp.blog.dto.response.BlogSummaryResponse;
import com.blogapp.blog.service.BlogService;
import com.blogapp.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Public blog APIs — listing, detail, archive")
public class BlogController {

    private final BlogService blogService;

    @GetMapping
    @Operation(summary = "Get published blogs", description = "Fetch published blogs with optional search, year/month filter, and sorting")
    public ResponseEntity<PageResponse<BlogSummaryResponse>> getPublishedBlogs(
            @Parameter(description = "Search keyword (searches title + excerpt)") @RequestParam(required = false) String search,
            @Parameter(description = "Filter by year (e.g., 2026)") @RequestParam(required = false) Integer year,
            @Parameter(description = "Filter by month (1–12)") @RequestParam(required = false) Integer month,
            @Parameter(description = "Sort order: recent (default), popular, oldest, most_commented") @RequestParam(required = false, defaultValue = "recent") String sort,
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(blogService.getPublishedBlogs(search, year, month, sort, page, size));
    }

    @GetMapping("/archive")
    @Operation(summary = "Get archive index", description = "Returns year → month breakdown with blog counts for the sidebar archive index")
    public ResponseEntity<List<ArchiveResponse>> getArchive() {
        return ResponseEntity.ok(blogService.getArchive());
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get blog by slug", description = "Fetch full blog detail by its URL-friendly slug")
    public ResponseEntity<BlogDetailResponse> getBlogBySlug(
            @Parameter(description = "Blog slug", example = "how-to-prepare-for-igcse-physics") @PathVariable String slug) {

        BlogDetailResponse blog = blogService.getBlogBySlug(slug);

        // Increment view count asynchronously (fire-and-forget)
        blogService.incrementViewCount(blog.getId());

        return ResponseEntity.ok(blog);
    }
}
