package com.viettel.vtskit.maria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MariaRepository<T, ID> extends JpaRepository<T, ID> {

}
