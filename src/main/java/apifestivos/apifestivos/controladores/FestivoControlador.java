package apifestivos.apifestivos.controladores;

import apifestivos.apifestivos.entidades.dtos.FestivoDto;
import apifestivos.apifestivos.servicios.FestivoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/festivos")
@CrossOrigin(origins = "*")
public class FestivoControlador {

    @Autowired
    private FestivoServicio festivoServicio;

    @GetMapping("/verificar/{año}/{mes}/{dia}")
    public ResponseEntity<String> verificarFestivo(@PathVariable int año, @PathVariable int mes, @PathVariable int dia) {
        String respuesta;
        if (festivoServicio.esFechaValida(String.format("%d-%02d-%02d", año, mes, dia))) {
            if (festivoServicio.esFestivo(new Date(año - 1900, mes - 1, dia))) {
                respuesta = "Es Festivo";
            } else {
                respuesta = "No es festivo";
            }
            return new ResponseEntity<>(respuesta, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Fecha no válida", HttpStatus.BAD_REQUEST);
        }
    }

   /*  @GetMapping("/obtener")
    public ResponseEntity<List<FestivoDto>> obtenerFestivos(@RequestParam int año) {
        List<FestivoDto> festivos = festivoServicio.obtenerFestivos(año);
        return new ResponseEntity<>(festivos, HttpStatus.OK);
    }*/

   /*  @PostMapping("/guardar")
    public ResponseEntity<Void> guardarFestivo(@RequestBody FestivoDto festivoDto) {
        festivoServicio.guardarFestivo(festivoDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Void> actualizarFestivo(@PathVariable Long id, @RequestBody FestivoDto festivoDto) {
        festivoServicio.actualizarFestivo(id, festivoDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarFestivo(@PathVariable Long id) {
        festivoServicio.eliminarFestivo(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/
}
