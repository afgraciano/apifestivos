package apifestivos.apifestivos.servicios;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
        int m = calcularM(age);
        int n = calcularN(age, m);

        int a = age % 19;
        int b = age % 4;
        int c = age % 7;
        int d = (19 * a + m) % 30;
        int e = (2 * b + 4 * c + 6 * d + n) % 7;

        int dia;
        int mes;

        if (d + e < 10) {
            dia = d + e + 22;
            mes = 3;
        } else {
            dia = d + e - 9;
            mes = 4;
        }

        corregirExcepcionesEspeciales(a, d, e, dia, mes);

        return new Date(age - 1900, mes - 1, dia);
    }

    private int calcularM(int age) {
        if (age >= 1583 && age <= 1699) {
            return 22;
        } else if (age >= 1700 && age <= 1899) {
            return 23;
        } else if (age >= 1900 && age <= 2099) {
            return 24;
        } else if (age >= 2100 && age <= 2299) {
            return 25;
        }
        return 0;
    }

    private int calcularN(int age, int m) {
        if (m == 22) {
            return 2;
        } else if (m == 23) {
            return (age <= 1799) ? 3 : 4;
        } else if (m == 24) {
            return (age <= 2199) ? 5 : 6;
        }
        return 0;
    }

    private void corregirExcepcionesEspeciales(int a, int d, int e, int dia, int mes) {
        if ((dia == 26 && mes == 4) || (dia == 25 && mes == 4 && d == 28 && e == 6 && a > 10)) {
            dia -= 7;
        }
    }

    private Date agregarDias(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(Calendar.DATE, dias);
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
    public List<FestivoDto> obtenerFestivos(int age) {
        List<Festivo> festivos = repositorio.findAll();
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
