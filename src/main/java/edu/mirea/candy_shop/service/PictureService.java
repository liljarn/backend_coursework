package edu.mirea.candy_shop.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

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

    @SneakyThrows
    public void putPicture(String picture, InputStream stream) {
        minioClient.putObject(PutObjectArgs
                .builder()
                .bucket(bucket)
                .object(picture.replaceAll(" ", "_").toLowerCase() + ".jpg")
                .stream(stream, -1, 10485760)
                .build());
    }
}
