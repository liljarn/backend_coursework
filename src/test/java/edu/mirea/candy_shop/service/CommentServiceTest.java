package edu.mirea.candy_shop.service;

import edu.mirea.candy_shop.dao.entity.CommentEntity;
import edu.mirea.candy_shop.dao.entity.CustomerEntity;
import edu.mirea.candy_shop.dao.repository.CommentRepository;
import edu.mirea.candy_shop.dao.repository.CustomerRepository;
import edu.mirea.candy_shop.dto.CommentDto;
import edu.mirea.candy_shop.dto.requests.AddCommentRequest;
import edu.mirea.candy_shop.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PictureService pictureService;

    private CustomerEntity customer;
    private CommentEntity comment;

    @BeforeEach
    void setUp() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        customer = new CustomerEntity();
        customer.setEmail("test@example.com");
        customer.setCustomerName("John");

        comment = new CommentEntity();
        comment.setCustomer(customer);
        comment.setCommentId(1L);
        comment.setCustomerName("John");
        comment.setComment("Test comment");
        comment.setRate(5);

        customer.getComments().add(comment);
    }

    @Test
    @DisplayName("CommentService#getAllComments test")
    void getAllComments_shouldReturnAllComments() {
        // Arrange
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(pictureService.getLinkOnCommentPicture(any())).thenReturn("dummy");
        List<CommentDto> expectedComments = new ArrayList<>();
        expectedComments.add(new CommentDto("John", "Test comment", URI.create("dummy"), 5));
        // Act
        List<CommentDto> actualComments = commentService.getAllComments();
        // Assert
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("CommentService#getComments test")
    void getComments_shouldReturnCustomerComments() {
        // Arrange
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(pictureService.getLinkOnCommentPicture(any())).thenReturn("dummy");
        List<CommentDto> expectedComments = new ArrayList<>();
        expectedComments.add(new CommentDto("John", "Test comment", URI.create("dummy"), 5));
        // Act
        List<CommentDto> actualComments = commentService.getComments("test@example.com");
        // Assert
        assertThat(actualComments).isEqualTo(expectedComments);
    }

    @Test
    @DisplayName("CommentService#addComment test")
    void addComment_shouldSuccessfullyAddNewComment() {
        // Arrange
        AddCommentRequest commentRequest = new AddCommentRequest("John", "Test comment", 5,
                new MockMultipartFile("image", new byte[0]));
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(commentRepository.save(any())).thenReturn(comment);
        // Act
        commentService.addComment("test@example.com", commentRequest);
        // Assert
        assertThat(customer.getComments()).contains(comment);
    }

    @Test
    @DisplayName("CommentService#deleteComment test")
    void deleteComment_shouldSuccessfullyDeleteComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        commentService.deleteComment("test@example.com", 1L);
        assertThat(customer.getComments()).doesNotContain(comment);
    }

    @Test
    @DisplayName("CommentService#deleteComment bad comment id test")
    void deleteComment_shouldThrowException_whenCommentNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        assertThatThrownBy(() -> commentService.deleteComment("test@example.com", 1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("CommentService#deleteComment bad email test")
    void deleteComment_shouldThrowException_whenCustomerNotFound() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> commentService.deleteComment("test@example.com", 1L))
                .isInstanceOf(CustomerNotFoundException.class);
    }
}
