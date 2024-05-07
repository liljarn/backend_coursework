package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.CommentDto;
import edu.mirea.candy_shop.dto.requests.AddCommentRequest;
import edu.mirea.candy_shop.dto.requests.DeleteCommentRequest;
import edu.mirea.candy_shop.service.CommentService;
import edu.mirea.candy_shop.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final JwtService jwtService;
    private final CommentService commentService;

    @GetMapping("/all")
    public List<CommentDto> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping
    public List<CommentDto> getComments(@RequestHeader("Token") String token) {
        String email = jwtService.extractUserName(token);
        return commentService.getComments(email);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void addComment(@RequestHeader("Token") String token, @ModelAttribute AddCommentRequest request) {
        String email = jwtService.extractUserName(token);
        commentService.addComment(email, request);
    }

    @DeleteMapping
    public void deleteComment(@RequestHeader("Token") String token, @RequestBody DeleteCommentRequest request) {
        String email = jwtService.extractUserName(token);
        commentService.deleteComment(email, request.id());
    }
}
