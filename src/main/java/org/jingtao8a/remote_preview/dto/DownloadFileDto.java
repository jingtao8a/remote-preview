package org.jingtao8a.remote_preview.dto;

import lombok.Data;

@Data
public class DownloadFileDto {
    private String downloadCode;
    private String fileName;
    private String filePath;
}
