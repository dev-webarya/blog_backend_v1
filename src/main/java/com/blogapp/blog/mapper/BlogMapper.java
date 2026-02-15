package com.blogapp.blog.mapper;

import com.blogapp.blog.dto.request.CreateBlogRequest;
import com.blogapp.blog.dto.response.BlogDetailResponse;
import com.blogapp.blog.dto.response.BlogSummaryResponse;
import com.blogapp.blog.entity.BlogPost;
import com.blogapp.blog.enums.BlogStatus;
import com.blogapp.common.util.HtmlSanitizer;
import com.blogapp.common.util.SlugUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BlogMapper {

    public BlogPost toEntity(CreateBlogRequest request) {
        return BlogPost.builder()
                .title(request.getTitle())
                .slug(SlugUtil.generateUniqueSlug(request.getTitle()))
                .excerpt(request.getExcerpt())
                .contentHtml(HtmlSanitizer.sanitize(request.getContentHtml()))
                .contentJson(request.getContentJson())
                .featuredImageUrl(request.getFeaturedImageUrl())
                .tags(request.getTags())
                .status(BlogStatus.DRAFT)
                .submittedAt(LocalDateTime.now())
                .build();
    }

    public BlogSummaryResponse toSummaryResponse(BlogPost entity) {
        return BlogSummaryResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .excerpt(entity.getExcerpt())
                .featuredImageUrl(entity.getFeaturedImageUrl())
                .authorName(entity.getAuthorName())
                .status(entity.getStatus())
                .publishedAt(entity.getPublishedAt())
                .tags(entity.getTags())
                .likesCount(entity.getLikesCount())
                .dislikesCount(entity.getDislikesCount())
                .commentsCount(entity.getCommentsCount())
                .viewsCount(entity.getViewsCount())
                .build();
    }

    public BlogDetailResponse toDetailResponse(BlogPost entity) {
        return BlogDetailResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .slug(entity.getSlug())
                .excerpt(entity.getExcerpt())
                .contentHtml(entity.getContentHtml())
                .contentJson(entity.getContentJson())
                .featuredImageUrl(entity.getFeaturedImageUrl())
                .authorName(entity.getAuthorName())
                .authorEmail(entity.getAuthorEmail())
                .status(entity.getStatus())
                .submittedAt(entity.getSubmittedAt())
                .publishedAt(entity.getPublishedAt())
                .rejectionReason(entity.getRejectionReason())
                .tags(entity.getTags())
                .likesCount(entity.getLikesCount())
                .dislikesCount(entity.getDislikesCount())
                .commentsCount(entity.getCommentsCount())
                .viewsCount(entity.getViewsCount())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
