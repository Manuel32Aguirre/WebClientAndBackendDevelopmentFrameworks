package domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    private int idEvento;
    private String nombre;
    private String descripcion;
    private LocalDate fechaEvento;
    private Integer idCategoria;
}
