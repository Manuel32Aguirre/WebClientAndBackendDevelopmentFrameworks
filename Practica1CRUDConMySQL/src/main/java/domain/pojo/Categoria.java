package domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    private int idCategoria;
    private String nombreCategoria;
    private String descripcion;

    @Override
    public String toString() {
        return nombreCategoria;
    }

}