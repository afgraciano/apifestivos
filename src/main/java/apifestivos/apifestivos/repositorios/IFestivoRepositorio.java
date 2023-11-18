package apifestivos.apifestivos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apifestivos.apifestivos.entidades.Festivo;




/*public class IFestivoRepositorio {

}



package apifestivos.apifestivos.repositorios;

import apifestivos.apifestivos.entidades.Festivo;
import org.springframework.data.jpa.repository.JpaRepository;
*/


@Repository
public interface IFestivoRepositorio extends JpaRepository<Festivo, Long>{
    // Puedes agregar métodos personalizados aquí si es necesario
}
