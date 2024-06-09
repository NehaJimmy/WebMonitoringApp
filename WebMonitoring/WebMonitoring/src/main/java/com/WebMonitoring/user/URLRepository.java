package com.WebMonitoring.user;

import com.WebMonitoring.entities.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URLRepository extends JpaRepository<URLEntity, Long> {
}

