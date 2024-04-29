package apifestivos.apifestivos.servicios;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apifestivos.apifestivos.interfaces.IFestivoServicio;
import apifestivos.apifestivos.entidades.Festivo;
import apifestivos.apifestivos.entidades.dtos.FestivoDto;
import apifestivos.apifestivos.repositorios.IFestivoRepositorio;

@Service
public class FestivoServicio implements IFestivoServicio {

    IFestivoRepositorio repositorio;

    public FestivoServicio(IFestivoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    private Date obtenerDomingoPascua(int age) {
        int M = 0;
        int N = 0;

        if (age >= 1583 && age <= 1699) {
            M = 22;
            N = 2;
        } else if (age >= 1700 && age <= 1899) {
            M = 23;
            N = (age <= 1799) ? 3 : 4;
        } else if (age >= 1900 && age <= 2099) {
            M = 24;
            N = (age <= 2199) ? 5 : 6;
        } else if (age >= 2100 && age <= 2299) {
            M = 25;
        }

        int A = age % 19;
        int B = age % 4;
        int C = age % 7;
        int D = (19 * A + M) % 30;
        int E = (2 * B + 4 * C + 6 * D + N) % 7;

        int mes;
        int dia;

        if (D + E < 10) {
            dia = D + E + 22;
            mes = 3; // Marzo
        } else {
            dia = D + E - 9;
            mes = 4; // Abril
        }

        // Excepciones especiales
        if (dia == 26 && mes == 4)
            dia = 19;
        if (dia == 25 && mes == 4 && D == 28 && E == 6 && A > 10)
            dia = 18;

        return new Date(age - 1900, mes - 1, dia);
    }

    private Date agregarDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, dias); // minus number would decrement the days
        return cal.getTime();
    }

    private Date siguienteLunes(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        if (c.get(Calendar.DAY_OF_WEEK) > Calendar.MONDAY)
            fecha = agregarDias(fecha, 9 - c.get(Calendar.DAY_OF_WEEK));
        else if (c.get(Calendar.DAY_OF_WEEK) < Calendar.MONDAY)
            fecha = agregarDias(fecha, 1);
        return fecha;
    }

    private List<Festivo> calcularFestivos(List<Festivo> festivos, int año) {
        if (festivos != null) {
            Date pascua = obtenerDomingoPascua(año);
            int i = 0;
            for (final Festivo festivo : festivos) {
                switch (festivo.getTipo().getId()) {
                    case 1:
                        festivo.setFecha(new Date(año - 1900, festivo.getMes() - 1, festivo.getDia()));
                        break;
                    case 2:
                        festivo.setFecha(siguienteLunes(new Date(año - 1900, festivo.getMes() - 1, festivo.getDia())));
                        break;
                    case 3:
                        festivo.setFecha(agregarDias(pascua, festivo.getDiasPascua()));
                        break;
                    case 4:
                        festivo.setFecha(siguienteLunes(agregarDias(pascua, festivo.getDiasPascua())));
                        break;
                    default:
                        break;
                }
                festivos.set(i, festivo);
                i++;
            }
        }
        return festivos;
    }

    @Override
    public List<FestivoDto> obtenerFestivos(int año) {
        List<Festivo> festivos = repositorio.findAll();
        festivos = calcularFestivos(festivos, año);
        List<FestivoDto> fechas = new ArrayList<FestivoDto>();
        for (final Festivo festivo : festivos) {
            fechas.add(new FestivoDto(festivo.getNombre(), festivo.getFecha()));
        }
        return fechas;
    }

    private boolean fechasIguales(Date fecha1, Date fecha2) {
        return fecha1.equals(fecha2);
    }

    private boolean esFestivo(List<Festivo> festivos, Date fecha) {
        if (festivos != null) {

            festivos = calcularFestivos(festivos, fecha.getYear() + 1900);

            for (final Festivo festivo : festivos) {
                if (fechasIguales(festivo.getFecha(), fecha))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean esFestivo(Date fecha) {
        List<Festivo> festivos = repositorio.findAll();
        return esFestivo(festivos, fecha);
    }

    @Override
    public boolean esFechaValida(String strFecha) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            df.parse(strFecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
