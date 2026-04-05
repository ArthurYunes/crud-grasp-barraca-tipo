package feira.graspcrud.service;

import feira.graspcrud.domain.TipoBarraca;
import feira.graspcrud.dto.TipoBarracaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.repository.TipoBarracaRepository;

import java.util.List;

/**
 * Serviço de aplicação responsável pelos casos de uso de {@link TipoBarraca}.
 *
 * <p>Padrão GRASP: Low Coupling — depende de {@link TipoBarracaRepository} e
 * {@link BarracaRepository} pelas interfaces, sem conhecer as implementações
 * concretas de persistência. Isso permite trocar a persistência JSON por outra
 * sem alterar esta classe.
 *
 * <p>Padrão GRASP: High Cohesion — esta classe trata exclusivamente dos casos
 * de uso relacionados a TipoBarraca, sem misturar responsabilidades de outras entidades.
 */
public class TipoBarracaService {

    private final TipoBarracaRepository repository;
    private final BarracaRepository barracaRepository;

    /**
     * Constrói o serviço com os repositórios necessários.
     *
     * @param repository        repositório de TipoBarraca (injetado pelo Main)
     * @param barracaRepository repositório de Barraca, usado para verificar se um tipo está em uso
     */
    public TipoBarracaService(TipoBarracaRepository repository, BarracaRepository barracaRepository) {
        this.repository = repository;
        this.barracaRepository = barracaRepository;
    }

    /**
     * Cadastra um novo TipoBarraca, garantindo unicidade do nome.
     *
     * <p>Regra: o nome do TipoBarraca deve ser único no sistema.
     *
     * @param request dados de entrada para criação do tipo de barraca
     * @return o TipoBarraca criado e persistido
     * @throws RegraNegocioException se já existir um tipo com o mesmo nome
     */
    public TipoBarraca cadastrar(TipoBarracaRequest request) {
        repository.buscarPorNome(request.getNome()).ifPresent(t -> {
            throw new RegraNegocioException(
                "Já existe um TipoBarraca com o nome '" + request.getNome() + "'.");
        });
        TipoBarraca tipo = new TipoBarraca(0, request.getNome());
        return repository.salvar(tipo);
    }

    /**
     * Lista todos os tipos de barraca cadastrados.
     *
     * @return lista de todos os TipoBarraca
     */
    public List<TipoBarraca> listar() {
        return repository.listarTodos();
    }

    /**
     * Busca um TipoBarraca pelo seu identificador.
     *
     * @param id identificador do tipo de barraca
     * @return o TipoBarraca encontrado
     * @throws RegraNegocioException se não existir TipoBarraca com o id informado
     */
    public TipoBarraca buscarPorId(int id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("TipoBarraca não encontrado com id: " + id));
    }

    /**
     * Remove um TipoBarraca pelo identificador, garantindo que não esteja em uso.
     *
     * <p>Regra: não é permitido remover um TipoBarraca que esteja associado a
     * alguma Barraca cadastrada no sistema.
     *
     * @param id identificador do tipo de barraca a remover
     * @throws RegraNegocioException se o TipoBarraca não existir ou estiver em uso por alguma Barraca
     */
    public void remover(int id) {
        buscarPorId(id); // garante que existe
        if (barracaRepository.existePorTipoBarracaId(id)) {
            throw new RegraNegocioException(
                "Não é possível remover o TipoBarraca de id " + id + " pois há barracas associadas a ele.");
        }
        repository.remover(id);
    }
}
