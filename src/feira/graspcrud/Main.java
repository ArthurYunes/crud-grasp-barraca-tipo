package feira.graspcrud;

import feira.graspcrud.controller.BarracaController;
import feira.graspcrud.repository.json.BarracaRepositoryJson;
import feira.graspcrud.repository.json.TipoBarracaRepositoryJson;
import feira.graspcrud.service.BarracaService;
import feira.graspcrud.service.TipoBarracaService;

import java.util.Scanner;

/**
 * Ponto de entrada da aplicação de gestão de feira livre — tema Barraca/TipoBarraca.
 *
 * <p>Padrão GRASP: Creator — esta classe é responsável por instanciar e
 * conectar todas as dependências manualmente (composição manual, sem framework de injeção),
 * pois é quem possui o contexto global necessário para isso.
 */
public class Main {

    /**
     * Método principal que inicializa repositórios, serviços e o controller,
     * e em seguida delega o controle do menu ao {@link BarracaController}.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Repositórios (Pure Fabrication + Indirection)
        TipoBarracaRepositoryJson tipoBarracaRepo = new TipoBarracaRepositoryJson();
        BarracaRepositoryJson barracaRepo = new BarracaRepositoryJson();

        // Serviços (Low Coupling — dependem de interfaces)
        TipoBarracaService tipoBarracaService = new TipoBarracaService(tipoBarracaRepo, barracaRepo);
        BarracaService barracaService = new BarracaService(barracaRepo, tipoBarracaRepo);

        // Controller (Controller GRASP)
        Scanner scanner = new Scanner(System.in);
        BarracaController controller = new BarracaController(barracaService, tipoBarracaService, scanner);

        controller.iniciar();
        scanner.close();
    }
}
