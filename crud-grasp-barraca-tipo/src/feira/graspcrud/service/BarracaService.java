package feira.graspcrud.service;

import feira.graspcrud.domain.Barraca;
import feira.graspcrud.dto.BarracaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.repository.TipoBarracaRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço de aplicação responsável pelos casos de uso de {@link Barraca}.
 *
 * <p>Padrão GRASP: Low Coupling — depende de {@link BarracaRepository} e
 * {@link TipoBarracaRepository} pelas interfaces, sem conhecer as implementações
 * concretas de persistência. Trocar a estratégia de persistência não impacta
 * esta classe.
 *
 * <p>Padrão GRASP: High Cohesion — trata exclusivamente dos casos de uso
 * relacionados a Barraca, mantendo responsabilidade única e bem definida.
 */
public class BarracaService {

    private final BarracaRepository barracaRepository;
    private final TipoBarracaRepository tipoBarracaRepository;

    /**
     * Constrói o serviço com os repositórios necessários.
     *
     * @param barracaRepository     repositório de Barraca
     * @param tipoBarracaRepository repositório de TipoBarraca (para validar existência)
     */
    public BarracaService(BarracaRepository barracaRepository, TipoBarracaRepository tipoBarracaRepository) {
        this.barracaRepository = barracaRepository;
        this.tipoBarracaRepository = tipoBarracaRepository;
    }

    /**
     * Cadastra uma nova Barraca, aplicando as regras de negócio do tema.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>O TipoBarraca informado deve existir.</li>
     *   <li>Não é permitido cadastrar duas barracas com o mesmo nome.</li>
     * </ul>
     *
     * @param request dados de entrada para criação da barraca
     * @return a Barraca criada e persistida
     * @throws RegraNegocioException se o TipoBarraca não existir ou o nome já estiver em uso
     */
    public Barraca cadastrar(BarracaRequest request) {
        validarTipoExistente(request.getTipoBarracaId());
        barracaRepository.buscarPorNome(request.getNome()).ifPresent(b -> {
            throw new RegraNegocioException(
                "Já existe uma Barraca com o nome '" + request.getNome() + "'.");
        });
        Barraca barraca = new Barraca(0, request.getNome(), request.isAtivo(), request.getTipoBarracaId());
        return barracaRepository.salvar(barraca);
    }

    /**
     * Lista apenas as barracas ativas (em operação).
     *
     * @return lista de barracas com ativo = true
     */
    public List<Barraca> listarAtivas() {
        return barracaRepository.listarTodos().stream()
                .filter(Barraca::isAtivo)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as barracas cadastradas, incluindo as inativas.
     *
     * @return lista completa de barracas
     */
    public List<Barraca> listarTodas() {
        return barracaRepository.listarTodos();
    }

    /**
     * Busca uma Barraca pelo seu identificador.
     *
     * @param id identificador da barraca
     * @return a Barraca encontrada
     * @throws RegraNegocioException se não existir Barraca com o id informado
     */
    public Barraca buscarPorId(int id) {
        return barracaRepository.buscarPorId(id)
                .orElseThrow(() -> new RegraNegocioException("Barraca não encontrada com id: " + id));
    }

    /**
     * Atualiza os dados de uma Barraca existente.
     *
     * <p>Regras aplicadas:
     * <ul>
     *   <li>A Barraca com o id informado deve existir.</li>
     *   <li>O TipoBarraca informado deve existir.</li>
     *   <li>O novo nome não pode estar em uso por outra Barraca.</li>
     * </ul>
     *
     * @param id      identificador da barraca a atualizar
     * @param request dados atualizados da barraca
     * @return a Barraca atualizada
     * @throws RegraNegocioException se alguma regra for violada
     */
    public Barraca atualizar(int id, BarracaRequest request) {
        Barraca existente = buscarPorId(id);
        validarTipoExistente(request.getTipoBarracaId());

        // Verifica unicidade de nome apenas se mudou
        if (!existente.getNome().equalsIgnoreCase(request.getNome())) {
            barracaRepository.buscarPorNome(request.getNome()).ifPresent(b -> {
                throw new RegraNegocioException(
                    "Já existe uma Barraca com o nome '" + request.getNome() + "'.");
            });
        }

        existente.setNome(request.getNome());
        existente.setAtivo(request.isAtivo());
        existente.setTipoBarracaId(request.getTipoBarracaId());
        barracaRepository.atualizar(existente);
        return existente;
    }

    /**
     * Remove uma Barraca pelo identificador.
     *
     * @param id identificador da barraca a remover
     * @throws RegraNegocioException se a Barraca não existir
     */
    public void remover(int id) {
        buscarPorId(id); // garante existência
        barracaRepository.remover(id);
    }

    /**
     * Valida que o TipoBarraca informado existe no repositório.
     *
     * @param tipoBarracaId id do tipo de barraca a validar
     * @throws RegraNegocioException se o TipoBarraca não for encontrado
     */
    private void validarTipoExistente(int tipoBarracaId) {
        tipoBarracaRepository.buscarPorId(tipoBarracaId)
                .orElseThrow(() -> new RegraNegocioException(
                    "TipoBarraca não encontrado com id: " + tipoBarracaId));
    }
}
