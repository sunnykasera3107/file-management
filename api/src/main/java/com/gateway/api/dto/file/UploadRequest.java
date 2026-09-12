package com.gateway.api.dto.file;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Upload file")
public class UploadRequest {
    
    @Schema(description = "Upload xlsx file", example="example.xlsx", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private MultipartFile file;
}
