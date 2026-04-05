package feira.graspcrud.domain;

import feira.graspcrud.exception.RegraNegocioException;

/**
 * Entidade de domínio que representa uma barraca na feira livre.
 *
 * <p>Padrão GRASP: Information Expert — esta classe valida suas próprias regras
 * de negócio (nome obrigatório, tipo obrigatório, regra de ativo), pois é quem
 * detém os dados necessários para essas validações.
 * Nenhuma regra de domínio de Barraca é delegada ao Controller ou ao Repositório.
 */
public class Barraca {

    private int id;
    private String nome;
    private boolean ativo;
    private int tipoBarracaId;

    /**
     * Construtor padrão necessário para desserialização JSON.
     */
    public Barraca() {}

    /**
     * Constrói uma Barraca com todos os campos, aplicando validações de domínio.
     *
     * @param id           identificador único
     * @param nome         nome da barraca (obrigatório, mínimo 3 caracteres)
     * @param ativo        indica se a barraca está em operação
     * @param tipoBarracaId identificador do {@link TipoBarraca} associado (obrigatório)
     * @throws RegraNegocioException se nome ou tipoBarracaId forem inválidos
     */
    public Barraca(int id, String nome, boolean ativo, int tipoBarracaId) {
        this.id = id;
        setNome(nome);
        this.ativo = ativo;
        setTipoBarracaId(tipoBarracaId);
    }

    /**
     * Define e valida o nome da barraca.
     *
     * <p>Regra: nome é obrigatório e deve ter no mínimo 3 caracteres.
     *
     * @param nome nome a ser atribuído
     * @throws RegraNegocioException se o nome for nulo, vazio ou com menos de 3 caracteres
     */
    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome da Barraca é obrigatório.");
        }
        if (nome.trim().length() < 3) {
            throw new RegraNegocioException("O nome da Barraca deve ter no mínimo 3 caracteres.");
        }
        this.nome = nome.trim();
    }

    /**
     * Define e valida o identificador do tipo de barraca.
     *
     * <p>Regra: o TipoBarraca é obrigatório no cadastro (id deve ser positivo).
     *
     * @param tipoBarracaId id do TipoBarraca
     * @throws RegraNegocioException se o id não for positivo
     */
    public void setTipoBarracaId(int tipoBarracaId) {
        if (tipoBarracaId <= 0) {
            throw new RegraNegocioException("O TipoBarraca é obrigatório para a Barraca.");
        }
        this.tipoBarracaId = tipoBarracaId;
    }

    /**
     * Retorna o identificador único da barraca.
     *
     * @return id da barraca
     */
    public int getId() { return id; }

    /**
     * Define o identificador único da barraca.
     *
     * @param id identificador a ser atribuído
     */
    public void setId(int id) { this.id = id; }

    /**
     * Retorna o nome da barraca.
     *
     * @return nome da barraca
     */
    public String getNome() { return nome; }

    /**
     * Retorna se a barraca está em operação.
     *
     * @return true se ativa, false se inativa
     */
    public boolean isAtivo() { return ativo; }

    /**
     * Define se a barraca está em operação.
     *
     * @param ativo true para ativa, false para inativa
     */
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    /**
     * Retorna o identificador do {@link TipoBarraca} associado.
     *
     * @return id do tipo de barraca
     */
    public int getTipoBarracaId() { return tipoBarracaId; }

    @Override
    public String toString() {
        return "[" + id + "] " + nome + " | Ativo: " + (ativo ? "Sim" : "Não") + " | TipoBarraca ID: " + tipoBarracaId;
    }
}
