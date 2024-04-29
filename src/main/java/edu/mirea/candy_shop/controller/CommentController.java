package edu.mirea.candy_shop.controller;

import edu.mirea.candy_shop.dto.CommentDto;
import edu.mirea.candy_shop.dto.requests.AddCommentRequest;
import edu.mirea.candy_shop.dto.requests.DeleteCommentRequest;
import edu.mirea.candy_shop.service.CommentService;
import edu.mirea.candy_shop.service.JwtService;
import edu.mirea.candy_shop.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final JwtService jwtService;
    private final CommentService commentService;
    private final PictureService pictureService;

    @GetMapping
    public List<CommentDto> getComments(@RequestHeader("Token") String token) {
        String email = jwtService.extractUserName(token);
        return commentService.getComments(email).stream().map(entity ->
                new CommentDto(
                        entity.getCustomerName(),
                        entity.getComment(),
                        URI.create(pictureService.getLinkOnCommentPicture(entity.getCommentId())),
                        entity.getRate())
        ).toList();
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
