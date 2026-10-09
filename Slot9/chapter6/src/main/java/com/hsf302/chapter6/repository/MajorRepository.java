package com.hsf302.chapter6.repository;

import com.hsf302.chapter6.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long> {}