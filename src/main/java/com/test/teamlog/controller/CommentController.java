package com.test.teamlog.controller;

import com.test.teamlog.entity.User;
import com.test.teamlog.payload.ApiResponse;
import com.test.teamlog.payload.CommentDTO;
import com.test.teamlog.payload.PagedResponse;
import com.test.teamlog.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Api(tags = "댓글 관리")
public class CommentController {
    private final CommentService commentService;

    @ApiOperation(value = "댓글 생성")
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse> createComment(@RequestBody CommentDTO.CommentRequest request,
                                                     @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = commentService.createComment(request, currentUser);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @ApiOperation(value = "댓글 수정")
    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable("id") long id,
                                                     @RequestBody CommentDTO.CommentUpdateRequest request,
                                                     @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = commentService.updateComment(id, request);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "댓글 삭제")
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("id") Long id,
                                                  @ApiIgnore @AuthenticationPrincipal User currentUser) {
        ApiResponse apiResponse = commentService.deleteComment(id);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "부모 댓글 조회")
    @GetMapping("/posts/{postId}/parent-comments")
    public ResponseEntity<PagedResponse<CommentDTO.CommentInfo>> getParentCommentsByPost(@PathVariable("postId") long postId,
                                                                                         @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                                         @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                                         @ApiIgnore @AuthenticationPrincipal User currentUser) {
        PagedResponse<CommentDTO.CommentInfo> response = commentService.getParentComments(postId, page, size, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "대댓글 조회")
    @GetMapping("/comments/{commentId}/child-comments")
    public ResponseEntity<PagedResponse<CommentDTO.CommentInfo>> getChildCommentsByPost(@PathVariable("commentId") long commentId,
                                                                                  @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                                  @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                                  @ApiIgnore @AuthenticationPrincipal User currentUser) {

        PagedResponse<CommentDTO.CommentInfo> response = commentService.getChildComments(commentId, page, size, currentUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}