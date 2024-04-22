package com.example.prueba_tecnica.Kafka;

import com.example.prueba_tecnica.dto.AuditoriaDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class Producer {
    public CompletableFuture<SendResult<String, AuditoriaDto>> send(KafkaTemplate<String, AuditoriaDto> kafkaTemplate, String nameTopic, AuditoriaDto auditoriaDto) {
        ListenableFuture<SendResult<String, AuditoriaDto>> listenableFuture = kafkaTemplate.send(nameTopic, auditoriaDto);
        // Convertir ListenableFuture a CompletableFuture
        return CompletableFuture.supplyAsync(() -> {
            try {
                return listenableFuture.get(); // Obtiene el resultado del ListenableFuture
            } catch (Exception e) {
                throw new RuntimeException(e); // Lanza una excepción si ocurre algún error
            }
        });
    }
    public AuditoriaDto setAuditoria (String tipoTransaccion, String usuario, BigDecimal monto, Long cuenta) {
        AuditoriaDto auditoria = AuditoriaDto.builder()
                .tipoTransaccion(tipoTransaccion)
                .fecha(new Date())
                .usuario(usuario)
                .descripcion("Se realizo un registro de transacction de " + tipoTransaccion)
                .monto(monto)
                .cuentaId(cuenta)
                .build();
        return auditoria;
    }
}
