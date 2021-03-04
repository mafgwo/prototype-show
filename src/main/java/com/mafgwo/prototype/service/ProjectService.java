package com.mafgwo.prototype.service;

import com.mafgwo.prototype.config.PrototypeProperties;
import com.mafgwo.prototype.constant.ProjectType;
import com.mafgwo.prototype.model.Project;
import com.mafgwo.prototype.repository.ProjectRepository;
import com.mafgwo.prototype.util.ArchiveUtilsExt;
import com.mafgwo.prototype.util.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.mafgwo.prototype.util.AssertUtils.checkParam;
import static com.mafgwo.prototype.util.AssertUtils.checkParamNotNull;

/**
 * 项目相关服务
 *
 * @author Mario Luo
 * @date 2019.01.19 10:51
 */
@Service
public class ProjectService {

    @Resource
    private PrototypeProperties config;

    @Resource
    private ProjectRepository repository;

    /**
     * 添加项目
     *
     * @param project
     * @return
     */
    public String saveProject(Project project, boolean edit) {
        checkSaveProjectParams(project, edit);
        String id = project.getId();
        if (StringUtils.isBlank(id)) {
            id = RandomStringUtils.random(8, "ABCDEFGHIJQMNOPQRSTUVWXYZ0123456789");
            project.setId(id);
        }
        long time = System.currentTimeMillis();
        if (!edit) {
            project.setCreateTime(time);
            project.setUploaded(false);
            project.setEntranceUri(null);
        }
        project.setUpdateTime(time);
        repository.save(project);
        return id;
    }

    private void checkSaveProjectParams(Project project, boolean edit) {
        String id = StringUtils.trim(project.getId());
        if (!edit && StringUtils.isNotEmpty(id)) {
            Project p = repository.findOne(id);
            checkParam(p == null, "ID已存在");
            checkParam(id.matches("^[A-Za-z0-9\\-_]*$"), "ID有效字符是字母或数字");
        }
        String name = StringUtils.trim(project.getName());
        int nameSize = StringUtils.length(name);
        checkParam(nameSize >= 2 && nameSize <= 12, "项目名称长度无效, 长度2~12字符");

        String creator = StringUtils.trim(project.getCreator());
        int creatorSize = StringUtils.length(creator);
        checkParam(creatorSize >= 2 && creatorSize <= 24, "创建人长度无效, 长度2~24字符");

        String description = StringUtils.trim(project.getDescription());
        int descSize = StringUtils.length(description);
        checkParam(descSize <= 512, "描述超出限定512个字符");

        // 重设参数
        project.setId(id);
        project.setName(name);
        project.setCreator(creator);
        project.setDescription(description);
    }

    /**
     * 获得所有项目
     *
     * @return
     */
    public List<Project> getProjects() {
        return repository.findAll();
    }

    /**
     * 获得单个项目
     *
     * @param id
     * @return
     */
    public Project getProject(String id) {
        return repository.findOne(id);
    }

    /**
     * 保存一个一个项目版本
     *
     * @param params
     * @param file
     */
    public void saveProjectFile(Project params, MultipartFile file) {
        Project project = checkSaveProjectFileParams(params, file);

        ProjectType type = ProjectType.of(params.getType());

        if (type == ProjectType.FILE) {
            // 存储文件
            String workDir = config.getWorkDir();
            String absWorkDir = new File(workDir).getAbsolutePath();
            String targetFilePath = absWorkDir + File.separator + PrototypeProperties.UPLOAD_DIR_NAME + File.separator + project.getId() + '-' + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            FileUtils.makeParent(targetFilePath);
            try {
                file.transferTo(new File(targetFilePath));
                // 解压到指定目录
                String projectPath = absWorkDir + File.separator + PrototypeProperties.PROJECTS_DIR_NAME + File.separator + project.getId() + File.separator;
                FileUtils.emptyDir(projectPath);
                ArchiveUtilsExt.unZip(targetFilePath, projectPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (type == ProjectType.LINK) {
            project.setLinkUrl(params.getLinkUrl());
        }

        project.setType(params.getType());
        project.setUploaded(true);
        project.setEntranceUri(params.getEntranceUri());
        project.setUpdateTime(System.currentTimeMillis());
        repository.save(project);
    }

    private Project checkSaveProjectFileParams(Project params, MultipartFile file) {
        params.setEntranceUri(StringUtils.trim(params.getEntranceUri()));
        params.setLinkUrl(StringUtils.trim(params.getLinkUrl()));

        String id = StringUtils.trim(params.getId());
        checkParam(StringUtils.isNotEmpty(id), "参数projectId不能为空");
        Project project = repository.findOne(id);
        checkParamNotNull(project, "项目不存在");

        ProjectType type = ProjectType.of(params.getType());
        checkParamNotNull(type, "参数type无效");
        if (type == ProjectType.FILE) {
            String entrance = params.getEntranceUri();
            if (StringUtils.isNotEmpty(entrance)) {
                checkParam(entrance.matches("^[A-Za-z0-9\\-_\\.]*$"), "入口地址, 只支持字母,数字,下划线, 小数点和中划线");
            }

            checkParamNotNull(file, "上传文件不能为空");
            checkParam(!file.isEmpty(), "上传文件不能为空");
        } else if (type == ProjectType.LINK) {
            checkParam(StringUtils.isNotEmpty(params.getLinkUrl()), "跳转链接不能为空");
        }

        return project;
    }

    /**
     * 获取存储文件目录
     *
     * @param id
     * @return
     */
    public String getProjectFilePath(String id) {
        String workDir = config.getWorkDir();
        String absWorkDir = new File(workDir).getAbsolutePath();
        String projectPath = absWorkDir + File.separator + PrototypeProperties.PROJECTS_DIR_NAME + File.separator + id;
        return projectPath;
    }

    public void delete(String id) {
        repository.delete(id);
    }
}
