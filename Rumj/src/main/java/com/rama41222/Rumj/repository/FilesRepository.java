package com.rama41222.Rumj.repository;

import com.rama41222.Rumj.files.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPA Repository
 */
public interface FilesRepository extends JpaRepository<File, Integer> {}
