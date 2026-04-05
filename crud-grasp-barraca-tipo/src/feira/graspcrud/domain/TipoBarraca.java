package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

/**
 * Entidade de domínio que representa o tipo (classificador) de uma barraca na feira livre.
 *
 * <p>Padrão GRASP: Information Expert — esta classe valida suas próprias regras
 * de consistência, pois é quem possui as informações necessárias para isso.
 * Nenhuma regra de domínio de TipoBarraca é colocada em Controller ou Repositório.
 */
public class TipoBarraca {

    private int id;
    private String nome;

    /**
     * Construtor padrão necessário para desserialização JSON.
     */
    public TipoBarraca() {}

    /**
     * Constrói um TipoBarraca com id e nome, aplicando validações de domínio.
     *
     * @param id   identificador único
     * @param nome nome do tipo de barraca (obrigatório, mínimo 3 caracteres)
     * @throws RegraNegocioException se o nome for inválido
     */
    public TipoBarraca(int id, String nome) {
        this.id = id;
        setNome(nome);
    }

    /**
     * Define e valida o nome do tipo de barraca.
     *
     * <p>Regra: nome é obrigatório e deve ter no mínimo 3 caracteres.
     *
     * @param nome nome a ser atribuído
     * @throws RegraNegocioException se o nome for nulo, vazio ou tiver menos de 3 caracteres
     */
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome do TipoBarraca é obrigatório.");
        }
        if (nome.trim().length() < 3) {
            throw new RegraNegocioException("O nome do TipoBarraca deve ter no mínimo 3 caracteres.");
        }
        this.nome = nome.trim();
    }

    /**
     * Retorna o identificador único do tipo de barraca.
     *
     * @return id do tipo de barraca
     */
    public int getId() { return id; }

    /**
     * Define o identificador único do tipo de barraca.
     *
     * @param id identificador a ser atribuído
     */
    public void setId(int id) { this.id = id; }

    /**
     * Retorna o nome do tipo de barraca.
     *
     * @return nome do tipo de barraca
     */
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return "[" + id + "] " + nome;
    }
}
