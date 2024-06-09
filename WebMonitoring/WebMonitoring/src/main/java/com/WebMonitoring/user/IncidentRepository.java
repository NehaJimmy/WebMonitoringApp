package com.WebMonitoring.user;

import com.WebMonitoring.entities.IncidentEntity;
import com.WebMonitoring.entities.URLEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<IncidentEntity, Long> {
    List<IncidentEntity> findByURL(URLEntity urlEntity);
}
