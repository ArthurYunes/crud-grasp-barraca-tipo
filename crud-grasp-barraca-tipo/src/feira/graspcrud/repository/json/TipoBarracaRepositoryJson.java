package feira.graspcrud.repository.json;

import feira.graspcrud.domain.TipoBarraca;
import feira.graspcrud.repository.TipoBarracaRepository;
import feira.graspcrud.util.JsonMini;

import java.util.*;

/**
 * Implementação de {@link TipoBarracaRepository} que persiste dados em arquivo JSON local.
 *
 * <p>Padrão GRASP: Pure Fabrication — esta classe não representa nenhum conceito
 * do domínio da feira; foi fabricada para isolar os detalhes de persistência JSON.
 *
 * <p>Padrão GRASP: Indirection — atua como intermediária entre os serviços (que
 * dependem da interface {@link TipoBarracaRepository}) e o mecanismo de armazenamento
 * em arquivo, desacoplando as camadas.
 */
public class TipoBarracaRepositoryJson implements TipoBarracaRepository {

    private static final String ARQUIVO = "data/tipos-barraca.json";
    private List<TipoBarraca> cache;
    private int proximoId;

    /**
     * Constrói o repositório carregando os dados do arquivo JSON ao iniciar.
     * Se o arquivo não existir, inicia com lista vazia.
     */
    public TipoBarracaRepositoryJson() {
        this.cache = new ArrayList<>();
        carregarDoArquivo();
        this.proximoId = cache.stream().mapToInt(TipoBarraca::getId).max().orElse(0) + 1;
    }

    @Override
    public TipoBarraca salvar(TipoBarraca tipo) {
        tipo.setId(proximoId++);
        cache.add(tipo);
        salvarNoArquivo();
        return tipo;
    }

    @Override
    public List<TipoBarraca> listarTodos() {
        return Collections.unmodifiableList(cache);
    }

    @Override
    public Optional<TipoBarraca> buscarPorId(int id) {
        return cache.stream().filter(t -> t.getId() == id).findFirst();
    }

    @Override
    public Optional<TipoBarraca> buscarPorNome(String nome) {
        return cache.stream()
                .filter(t -> t.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    @Override
    public void remover(int id) {
        cache.removeIf(t -> t.getId() == id);
        salvarNoArquivo();
    }

    // -------------------------------------------------------------------------
    // Métodos privados de persistência
    // -------------------------------------------------------------------------

    /**
     * Carrega a lista de TipoBarraca do arquivo JSON para o cache em memória.
     * Registros com dados corrompidos são ignorados com aviso no console.
     */
    private void carregarDoArquivo() {
        List<Map<String, String>> lista = JsonMini.lerLista(ARQUIVO);
        for (Map<String, String> mapa : lista) {
            try {
                TipoBarraca t = new TipoBarraca();
                t.setId(Integer.parseInt(mapa.get("id")));
                t.setNome(mapa.get("nome"));
                cache.add(t);
            } catch (Exception e) {
                System.err.println("[AVISO] Registro de TipoBarraca ignorado por dado inválido: " + mapa + " — " + e.getMessage());
            }
        }
    }

    /**
     * Persiste o cache atual de TipoBarraca no arquivo JSON.
     * Chamado após cada operação de escrita.
     */
    private void salvarNoArquivo() {
        List<Map<String, String>> lista = new ArrayList<>();
        for (TipoBarraca t : cache) {
            Map<String, String> mapa = new LinkedHashMap<>();
            mapa.put("id", String.valueOf(t.getId()));
            mapa.put("nome", t.getNome());
            lista.add(mapa);
        }
        JsonMini.escreverLista(ARQUIVO, lista);
    }
}
