package com.quickpolls.core.repository;

import com.quickpolls.core.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {}