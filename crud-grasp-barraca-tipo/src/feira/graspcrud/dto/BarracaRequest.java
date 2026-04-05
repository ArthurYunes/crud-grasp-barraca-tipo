package feira.graspcrud.dto;

/**
 * DTO (Data Transfer Object) que transporta os dados de entrada para
 * criação ou atualização de uma {@link feira.graspcrud.domain.Barraca}.
 *
 * <p>Padrão GRASP: Low Coupling — separa os dados de entrada da entidade
 * de domínio, evitando que o Controller acople-se diretamente à Barraca.
 */
public class BarracaRequest {

    private String nome;
    private boolean ativo;
    private int tipoBarracaId;

    /**
     * Constrói o request com todos os campos necessários para criar ou atualizar uma Barraca.
     *
     * @param nome          nome da barraca
     * @param ativo         se a barraca está em operação
     * @param tipoBarracaId id do TipoBarraca associado
     */
    public BarracaRequest(String nome, boolean ativo, int tipoBarracaId) {
        this.nome = nome;
        this.ativo = ativo;
        this.tipoBarracaId = tipoBarracaId;
    }

    /**
     * Retorna o nome informado para a barraca.
     *
     * @return nome da barraca
     */
    public String getNome() { return nome; }

    /**
     * Retorna se a barraca deve ser criada como ativa.
     *
     * @return true se ativa
     */
    public boolean isAtivo() { return ativo; }

    /**
     * Retorna o id do TipoBarraca informado.
     *
     * @return id do tipo de barraca
     */
    public int getTipoBarracaId() { return tipoBarracaId; }
}
