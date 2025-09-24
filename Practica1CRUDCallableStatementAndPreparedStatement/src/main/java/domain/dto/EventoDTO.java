package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class EventoDTO {
    private int idEvento;
    private String nombre;
    private String descripcion;
    private LocalDate fechaEvento;
    private String nombreCategoria;
}
