package feira.graspcrud.repository;

import feira.graspcrud.domain.Barraca;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para {@link Barraca}.
 *
 * <p>Padrão GRASP: Protected Variations — ao depender desta interface,
 * os serviços ficam protegidos de variações na implementação de persistência.
 * Trocar JSON por banco de dados requer apenas uma nova implementação desta interface,
 * sem impactar domínio ou serviços.
 */
public interface BarracaRepository {

    /**
     * Persiste uma nova Barraca.
     *
     * @param barraca a barraca a ser salva
     * @return a barraca salva com id gerado
     */
    Barraca salvar(Barraca barraca);

    /**
     * Retorna todas as barracas cadastradas, incluindo inativas.
     *
     * @return lista de todas as barracas
     */
    List<Barraca> listarTodos();

    /**
     * Busca uma Barraca pelo seu identificador.
     *
     * @param id identificador da barraca
     * @return Optional contendo a barraca encontrada, ou vazio se não existir
     */
    Optional<Barraca> buscarPorId(int id);

    /**
     * Busca uma Barraca pelo nome (para verificar unicidade).
     *
     * @param nome nome a ser pesquisado
     * @return Optional contendo a barraca encontrada, ou vazio se não existir
     */
    Optional<Barraca> buscarPorNome(String nome);

    /**
     * Atualiza os dados de uma Barraca existente.
     *
     * @param barraca a barraca com dados atualizados (deve ter id existente)
     */
    void atualizar(Barraca barraca);

    /**
     * Remove a Barraca com o id informado.
     *
     * @param id identificador da barraca a ser removida
     */
    void remover(int id);

    /**
     * Verifica se existe alguma Barraca associada ao TipoBarraca informado.
     *
     * @param tipoBarracaId id do tipo de barraca
     * @return true se houver ao menos uma barraca com esse tipo
     */
    boolean existePorTipoBarracaId(int tipoBarracaId);
}
