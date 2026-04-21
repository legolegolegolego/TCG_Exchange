package com.es.tcg_exchange.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.es.tcg_exchange.dto.CloudinaryImage;
import com.es.tcg_exchange.error.exception.InternalServerErrorException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ImagenService {

    private final Cloudinary cloudinary;

    public ImagenService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public CloudinaryImage subirImagen(MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.emptyMap());

            return new CloudinaryImage(
                    result.get("secure_url").toString(),
                    result.get("public_id").toString()
            );

        } catch (Exception e) {
            throw new InternalServerErrorException("Error subiendo imagen");
        }
    }

    public void eliminarImagen(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new InternalServerErrorException("Error eliminando imagen");
        }
    }
}