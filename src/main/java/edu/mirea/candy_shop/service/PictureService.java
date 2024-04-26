package edu.mirea.candy_shop.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PictureService {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @SneakyThrows
    public String getLinkOnPicture(String picture) {
        return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs
                .builder()
                .bucket(bucket)
                .object(picture.replaceAll("\\s", "_").toLowerCase() + ".jpg")
                .expiry(3600)
                .method(Method.GET)
                .build());
    }
}
