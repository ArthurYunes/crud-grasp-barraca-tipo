package feira.graspcrud.repository;

import feira.graspcrud.domain.TipoBarraca;
import java.util.List;
import java.util.Optional;

/**
 * Interface de repositório para {@link TipoBarraca}.
 *
 * <p>Padrão GRASP: Protected Variations — ao depender desta interface,
 * os serviços ficam isolados de qualquer mudança na implementação de persistência.
 * Trocar JSON por outro mecanismo requer apenas criar uma nova implementação,
 * sem alterar domínio ou serviços.
 */
public interface TipoBarracaRepository {

    /**
     * Persiste um novo TipoBarraca.
     *
     * @param tipo o tipo de barraca a ser salvo
     * @return o tipo salvo com id gerado
     */
    TipoBarraca salvar(TipoBarraca tipo);

    /**
     * Retorna todos os tipos de barraca cadastrados.
     *
     * @return lista de todos os tipos de barraca
     */
    List<TipoBarraca> listarTodos();

    /**
     * Busca um TipoBarraca pelo seu identificador.
     *
     * @param id identificador do tipo de barraca
     * @return Optional contendo o tipo encontrado, ou vazio se não existir
     */
    Optional<TipoBarraca> buscarPorId(int id);

    /**
     * Busca um TipoBarraca pelo nome (para verificar unicidade).
     *
     * @param nome nome a ser pesquisado
     * @return Optional contendo o tipo encontrado, ou vazio se não existir
     */
    Optional<TipoBarraca> buscarPorNome(String nome);

    /**
     * Remove o TipoBarraca com o id informado.
     *
     * @param id identificador do tipo a ser removido
     */
    void remover(int id);
}
