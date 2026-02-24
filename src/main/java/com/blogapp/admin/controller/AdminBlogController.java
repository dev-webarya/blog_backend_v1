package com.blogapp.admin.controller;

import com.blogapp.admin.dto.request.ApproveBlogRequest;
import com.blogapp.admin.dto.request.RejectBlogRequest;
import com.blogapp.blog.dto.request.CreateBlogRequest;
import com.blogapp.blog.dto.response.BlogDetailResponse;
import com.blogapp.blog.entity.BlogPost;
import com.blogapp.blog.mapper.BlogMapper;
import com.blogapp.blog.service.BlogService;
import com.blogapp.comment.service.CommentService;
import com.blogapp.common.dto.PageResponse;
import com.blogapp.otp.service.EmailService;
import com.blogapp.subscriber.dto.response.SubscriberResponse;
import com.blogapp.subscriber.service.SubscriberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin - Blog Moderation", description = "Admin endpoints for blog approval, rejection, and management")
public class AdminBlogController {

    private final BlogService blogService;
    private final BlogMapper blogMapper;
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    @GetMapping("/blogs")
    @Operation(summary = "Get blogs by status", description = "List blogs filtered by status (PENDING, PUBLISHED, REJECTED, DRAFT). "
            + "If no status is provided, returns all blogs.")
    public ResponseEntity<PageResponse<BlogDetailResponse>> getAdminBlogs(
            @Parameter(description = "Filter by status: PENDING, PUBLISHED, REJECTED, DRAFT") @RequestParam(required = false) String status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(blogService.getAdminBlogs(status, page, size));
    }

    @GetMapping("/blogs/{id}")
    @Operation(summary = "Get blog details by ID", description = "View full blog details including author email/mobile — admin only")
    public ResponseEntity<BlogDetailResponse> getBlogById(
            @Parameter(description = "Blog ID") @PathVariable String id) {

        return ResponseEntity.ok(blogService.getBlogById(id));
    }

    @PostMapping("/blogs/{id}/approve")
    @Operation(summary = "Approve a blog", description = "Approve a PENDING blog. Sets status to PUBLISHED and sends notification email to author.")
    public ResponseEntity<BlogDetailResponse> approveBlog(
            @Parameter(description = "Blog ID") @PathVariable String id,
            @RequestBody(required = false) ApproveBlogRequest request) {

        String adminId = (request != null && request.getAdminId() != null)
                ? request.getAdminId()
                : "admin";

        BlogPost approvedBlog = blogService.approveBlog(id, adminId);

        // Send approval email
        String blogLink = "/blog/" + approvedBlog.getSlug();
        emailService.sendApprovalEmail(approvedBlog.getAuthorEmail(),
                approvedBlog.getTitle(), blogLink);

        return ResponseEntity.ok(blogMapper.toDetailResponse(approvedBlog));
    }

    @PostMapping("/blogs/{id}/reject")
    @Operation(summary = "Reject a blog", description = "Reject a PENDING blog. Reason is required. Sends rejection email to author.")
    public ResponseEntity<BlogDetailResponse> rejectBlog(
            @Parameter(description = "Blog ID") @PathVariable String id,
            @Valid @RequestBody RejectBlogRequest request) {

        BlogPost rejectedBlog = blogService.rejectBlog(id, request.getReason());

        // Send rejection email
        emailService.sendRejectionEmail(rejectedBlog.getAuthorEmail(),
                rejectedBlog.getTitle(), request.getReason());

        return ResponseEntity.ok(blogMapper.toDetailResponse(rejectedBlog));
    }

    @PatchMapping("/blogs/{id}")
    @Operation(summary = "Edit a blog", description = "Admin can edit blog content before or after publishing")
    public ResponseEntity<BlogDetailResponse> editBlog(
            @Parameter(description = "Blog ID") @PathVariable String id,
            @Valid @RequestBody CreateBlogRequest request) {

        BlogPost updatedBlog = blogService.updateBlog(id, request);
        return ResponseEntity.ok(blogMapper.toDetailResponse(updatedBlog));
    }

    @DeleteMapping("/blogs/{id}")
    @Operation(summary = "Delete a blog", description = "Delete a blog by ID. Only the author or an admin can delete.")
    public ResponseEntity<Void> deleteBlog(
            @Parameter(description = "Blog ID") @PathVariable String id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscribers")
    @Operation(summary = "Get all subscribers", description = "List all subscribers with optional status filter")
    public ResponseEntity<PageResponse<SubscriberResponse>> getSubscribers(
            @Parameter(description = "Filter by status: ACTIVE, UNSUBSCRIBED") @RequestParam(required = false) String status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(subscriberService.getAllSubscribers(status, page, size));
    }
}
