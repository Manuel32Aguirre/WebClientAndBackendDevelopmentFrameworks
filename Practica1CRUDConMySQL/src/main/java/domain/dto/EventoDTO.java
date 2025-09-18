package domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class EventoDTO {
    private int idEvento;
    private String nombre;
    private String descripcion;
    private LocalDate fechaEvento;
    private String nombreCategoria;
    Connection conexion;

    private static final String SQL_SELECT_ALL_EVENTS_WITH_CATEGORIA = " { call select_all_events_with_categoria() } ";

    public EventoDTO(Connection conexion){
        this.conexion = conexion;
    }

    public void select_all_events_with_categoria(){
        try(CallableStatement cs = conexion.prepareCall(SQL_SELECT_ALL_EVENTS_WITH_CATEGORIA)){
            ResultSet rs = cs.executeQuery();
            while(rs.next()){

            }
        }catch (SQLException ex){
            Logger.getLogger(EventoDTO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
