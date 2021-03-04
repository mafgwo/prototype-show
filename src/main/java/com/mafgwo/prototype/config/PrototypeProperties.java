package com.mafgwo.prototype.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.mafgwo.prototype.config.PrototypeProperties.PREFIX;

/**
 * PrototypeShowConfiguration
 *
 * @author Mario Luo
 * @date 2019.01.19 13:28
 */
@Component
@ConfigurationProperties(prefix = PREFIX)
@Data
public class PrototypeProperties {

    public static final String PREFIX = "prototype-show";
    public static final String PROJECTS_DIR_NAME = "projects";
    public static final String UPLOAD_DIR_NAME = "upload";
    public static final String PROJECT_META_FILE_NAME = "project.json";

    /** 数据存储目录 */
    private String workDir = System.getProperty("user.home") + File.separator + ".prototype-show";

}
