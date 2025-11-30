package com.aprobaciones.repository;

import com.aprobaciones.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {
    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.userId = :userId OR s.aprobador.userId = :userId")
    List<Solicitud> findBySolicitanteIdOrAprobadorId(@Param("userId") String userId);
}