package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Wilder;

@Repository
public interface WilderRepository extends JpaRepository<Wilder, Long> {
}