package feira.graspcrud.dto;

/**
 * DTO (Data Transfer Object) que transporta os dados de entrada para
 * criação ou atualização de um {@link feira.graspcrud.domain.TipoBarraca}.
 *
 * <p>Padrão GRASP: Low Coupling — isola os dados de entrada da entidade
 * de domínio, mantendo o Controller desacoplado da classe TipoBarraca.
 */
public class TipoBarracaRequest {

    private String nome;

    /**
     * Constrói o request com o nome do tipo de barraca.
     *
     * @param nome nome do tipo de barraca
     */
    public TipoBarracaRequest(String nome) {
        this.nome = nome;
    }

    /**
     * Retorna o nome informado para o tipo de barraca.
     *
     * @return nome do tipo de barraca
     */
    public String getNome() { return nome; }
}
