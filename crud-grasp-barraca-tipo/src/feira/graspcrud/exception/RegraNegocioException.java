package feira.graspcrud.exception;

/**
 * Exceção de domínio lançada quando uma regra de negócio é violada.
 *
 * <p>Padrão GRASP: Information Expert — esta exceção encapsula apenas a
 * mensagem de violação de regra, sem qualquer dependência de infraestrutura
 * ou framework, mantendo o domínio isolado.
 */
public class RegraNegocioException extends RuntimeException {

    /**
     * Constrói a exceção com a mensagem descritiva da regra violada.
     *
     * @param mensagem descrição da regra de negócio violada
     */
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
