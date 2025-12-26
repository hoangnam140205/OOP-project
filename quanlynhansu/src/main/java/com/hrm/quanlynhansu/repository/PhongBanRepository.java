package com.hrm.quanlynhansu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hrm.quanlynhansu.entity.PhongBan;

@Repository
public interface PhongBanRepository extends JpaRepository<PhongBan, String> {
}
