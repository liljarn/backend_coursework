package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CommentEntity;
import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.repository.CommentRepository;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dto.CommentDto;
import edu.mirea.candy_shop.dto.requests.AddCommentRequest;
import edu.mirea.candy_shop.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CustomerRepository customerRepository;
    private final CommentRepository commentRepository;
    private final PictureService pictureService;

    @Transactional
    public List<CommentDto> getAllComments() {
        return customerRepository.findAll().stream()
                .flatMap(customerEntity -> customerEntity.getComments().stream()
                        .map(commentEntity -> new CommentDto(
                                commentEntity.getCustomerName(),
                                commentEntity.getComment(),
                                URI.create(pictureService.getLinkOnCommentPicture(commentEntity.getCommentId())),
                                commentEntity.getRate()
                        )))
                .toList();
    }

    @Transactional
    public List<CommentDto> getComments(String email) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        return customer.getComments().stream().map(entity ->
                new CommentDto(
                        entity.getCustomerName(),
                        entity.getComment(),
                        URI.create(pictureService.getLinkOnCommentPicture(entity.getCommentId())),
                        entity.getRate())
        ).toList();
    }

    @Transactional
    @SneakyThrows
    public void addComment(String email, AddCommentRequest commentRequest) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        CommentEntity comment = new CommentEntity(
                commentRequest.customerName(),
                commentRequest.comment(),
                commentRequest.rate()
        );
        comment.setCustomer(customer);
        commentRepository.save(comment);
        pictureService.putCommentPicture(comment.getCommentId(), commentRequest.image().getInputStream());
        customer.getComments().add(comment);
    }


    @Transactional
    public void deleteComment(String email, Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(RuntimeException::new);
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(CustomerNotFoundException::new);
        customer.getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
