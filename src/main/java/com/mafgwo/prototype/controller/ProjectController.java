package com.mafgwo.prototype.controller;

import com.mafgwo.prototype.model.Project;
import com.mafgwo.prototype.service.ProjectService;
import com.mafgwo.prototype.util.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 项目控制器
 *
 * @author Mario Luo
 * @date 2019.01.19 10:46
 */
@Slf4j
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @PostMapping
    public String addProject(Project project, boolean edit, HttpServletRequest request) throws IOException {
        log.info("add or update project id: {}, name: {}, ip: {}", project.getId(), project.getName(), NetUtils.getIpAddress(request));
        return projectService.saveProject(project, edit);
    }

    @GetMapping
    public List<Project> listProject() {
        return projectService.getProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable String id) {
        return projectService.getProject(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id, HttpServletRequest request) throws IOException {
        log.info("delete project id: {}, ip: {}", id, NetUtils.getIpAddress(request));
        projectService.delete(id);
    }

    @PostMapping("/{id}/upload")
    public void upload(@PathVariable String id, @RequestParam(required = false, defaultValue = "1") Integer type, String linkUrl, String entranceUri, MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("upload prototype, projectId: {}, ip: {}", id, NetUtils.getIpAddress(request));
        Project project = new Project();
        project.setId(id);
        project.setEntranceUri(entranceUri);
        project.setType(type);
        project.setLinkUrl(linkUrl);
        projectService.saveProjectFile(project, file);
    }
}
