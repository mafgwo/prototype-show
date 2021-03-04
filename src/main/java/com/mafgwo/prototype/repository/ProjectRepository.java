package com.mafgwo.prototype.repository;

import com.mafgwo.prototype.model.Project;

import java.util.List;

/**
 * ProjectRepository
 *
 * @author Mario Luo
 * @date 2019.01.19 11:23
 */
public interface ProjectRepository {

    Project findOne(String id);

    int save(Project project);

    List<Project> findAll();

    int delete(String id);
}
