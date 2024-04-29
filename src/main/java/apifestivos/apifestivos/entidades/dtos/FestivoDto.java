package apifestivos.apifestivos.entidades.dtos;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FestivoDto {

    private String nombre;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    private int diasPascua;

    public FestivoDto() {
    }

    public FestivoDto(String festivo, Date fecha) {
        this.nombre = festivo;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getDiasPascua() {
        return diasPascua;
    }

    public void setDiasPascua(int diasPascua) {
        this.diasPascua = diasPascua;
    }

    public String getFestivo() {
        return nombre;
    }

    public void setFestivo(String festivo) {
        this.nombre = festivo;
    }

}
