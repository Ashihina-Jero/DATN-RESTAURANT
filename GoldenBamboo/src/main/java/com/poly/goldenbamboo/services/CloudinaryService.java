package com.poly.goldenbamboo.services;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), 
                ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "golden_bamboo/combos"
                ));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            logger.error("Failed to upload file to Cloudinary", e);
            throw new IOException("Failed to upload file to Cloudinary", e);
        }
    }

    public void deleteFile(String publicId) throws IOException {
        try {
            if (publicId != null && !publicId.isEmpty()) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException e) {
            logger.error("Failed to delete file from Cloudinary: " + publicId, e);
            throw new IOException("Failed to delete file from Cloudinary", e);
        }
    }

    public String extractPublicIdFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }
        
        try {
       
            String[] parts = imageUrl.split("/");
            
            int uploadIndex = -1;
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("upload")) {
                    uploadIndex = i;
                    break;
                }
            }
            
            if (uploadIndex == -1 || uploadIndex >= parts.length - 2) {
                return null;
            }
            
            StringBuilder publicIdBuilder = new StringBuilder();
            for (int i = uploadIndex + 2; i < parts.length; i++) {
                if (i > uploadIndex + 2) {
                    publicIdBuilder.append("/");
                }
                publicIdBuilder.append(parts[i]);
            }
            
            String publicId = publicIdBuilder.toString();
            int lastDotIndex = publicId.lastIndexOf('.');
            if (lastDotIndex > 0) {
                publicId = publicId.substring(0, lastDotIndex);
            }
            
            return publicId;
        } catch (Exception e) {
            logger.warn("Invalid Cloudinary URL format: " + imageUrl, e);
            return null;
        }
    }
}