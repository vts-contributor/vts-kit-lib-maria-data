package com.viettel.vtskit.maria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MariaRepository<T, I> extends JpaRepository<T, I> {

}
