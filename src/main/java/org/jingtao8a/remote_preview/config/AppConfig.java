package org.jingtao8a.remote_preview.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class AppConfig {
    @Value("${project.folder}")
    private String projectFolder;

    @Value("${log.root.level}")
    private String logRootLevel;

    @Value("${remote.preview.material}")
    private String remotePreviewMaterial;
}
