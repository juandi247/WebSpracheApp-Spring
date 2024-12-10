package com.sprache.juandiegodeutsch.repositories;


import com.sprache.juandiegodeutsch.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template,Long> {

    boolean existsByName(String name);

    Optional<Template> findByName(String name);

}
