package apifestivos.apifestivos.controladores;

import apifestivos.apifestivos.entidades.dtos.FestivoDto;
import apifestivos.apifestivos.servicios.FestivoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/festivos")

@CrossOrigin(origins = "*")
public class FestivoControlador {

    private FestivoServicio festivoServicio;

    public FestivoControlador(FestivoServicio festivoServicio) {
        this.festivoServicio = festivoServicio;
    }

    @GetMapping("/verificar/{age}/{mes}/{dia}")
    public ResponseEntity<Map<String, String>> verificarFestivo(@PathVariable int age, @PathVariable int mes,
            @PathVariable int dia) {
        final String m = "mensaje";
        Map<String, String> respuesta = new HashMap<>();
        if (festivoServicio.esFechaValida(String.format("%d-%02d-%02d", age, mes, dia))) {

            if (festivoServicio.esFestivo(new Date(age - 1900, mes - 1, dia))) {
                respuesta.put(m, "Es Festivo");
            } else {
                respuesta.put(m, "No es festivo");
            }
        } else {
            respuesta.put(m, "Fecha no válida");
        }
        return ResponseEntity.ok(respuesta);
    }

    @RequestMapping(value = "/obtener/{age}", method = RequestMethod.GET)
    public ResponseEntity<List<FestivoDto>> obtener(@PathVariable int age) {
        List<FestivoDto> festivos = festivoServicio.obtenerFestivos(age);

        if (festivos != null && !festivos.isEmpty()) {
            return new ResponseEntity<>(festivos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
