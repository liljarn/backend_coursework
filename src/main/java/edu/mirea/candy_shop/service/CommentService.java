package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CommentEntity;
import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.repository.CommentRepository;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dto.requests.AddCommentRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final PictureService pictureService;

    @Transactional
    public List<CommentEntity> getComments(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return customer.getComments();
    }

    @Transactional
    @SneakyThrows
    public void addComment(String email, AddCommentRequest commentRequest) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        CommentEntity comment = new CommentEntity(
                commentRequest.customerName(),
                commentRequest.comment(),
                commentRequest.rate()
        );
        commentRepository.save(comment);
        pictureService.putCommentPicture(comment.getCommentId(), commentRequest.image().getInputStream());
        customer.getComments().add(comment);
        comment.setCustomer(customer);
    }

    @Transactional
    public void deleteComment(String email, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        customer.getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
