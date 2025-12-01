package com.aprobaciones.repository;

import com.aprobaciones.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface SolicitudRepository extends JpaRepository<Solicitud, UUID> {

    @Query("SELECT s FROM Solicitud s WHERE s.aprobador.userId = :userId")
    List<Solicitud> buscarPorAprobador(@Param("userId") String userId);
    
    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.userId = :solicitanteId OR s.aprobador.userId = :aprobadorId")
    List<Solicitud> buscarPorSolicitanteOAprobador(@Param("solicitanteId") String solicitanteId, @Param("aprobadorId") String aprobadorId);

    @Query("SELECT s FROM Solicitud s WHERE s.solicitante.userId = :solicitanteId")
    List<Solicitud> buscarPorSolicitante(@Param("solicitanteId") String solicitanteId);
}