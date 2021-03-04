package com.mafgwo.prototype.config;

import com.mafgwo.prototype.util.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.io.File;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Resource
    PrototypeProperties properties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String workDir = properties.getWorkDir();
        FileUtils.makeParent(workDir);
        File dir = new File(workDir);
        String path = dir.getAbsolutePath();
        String url = "/v/**";
        String location = "file:" + path + File.separator + PrototypeProperties.PROJECTS_DIR_NAME + File.separator;
        registry.addResourceHandler(url).addResourceLocations(location);
        super.addResourceHandlers(registry);
    }
}
