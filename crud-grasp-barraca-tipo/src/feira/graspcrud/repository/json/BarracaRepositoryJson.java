package feira.graspcrud.repository.json;

import feira.graspcrud.domain.Barraca;
import feira.graspcrud.repository.BarracaRepository;
import feira.graspcrud.util.JsonMini;

import java.util.*;

/**
 * Implementação de {@link BarracaRepository} que persiste dados em arquivo JSON local.
 *
 * <p>Padrão GRASP: Pure Fabrication — esta classe não representa nenhum conceito
 * do domínio da feira livre; foi fabricada para isolar os detalhes técnicos de
 * leitura e escrita em arquivo JSON.
 *
 * <p>Padrão GRASP: Indirection — age como intermediária entre os serviços
 * (que dependem da interface {@link BarracaRepository}) e o arquivo JSON,
 * protegendo o domínio de qualquer detalhe de infraestrutura.
 */
public class BarracaRepositoryJson implements BarracaRepository {

    private static final String ARQUIVO = "data/barracas.json";
    private List<Barraca> cache;
    private int proximoId;

    /**
     * Constrói o repositório carregando os dados do arquivo JSON ao iniciar.
     * Se o arquivo não existir, inicia com lista vazia.
     */
    public BarracaRepositoryJson() {
        this.cache = new ArrayList<>();
        carregarDoArquivo();
        this.proximoId = cache.stream().mapToInt(Barraca::getId).max().orElse(0) + 1;
    }

    @Override
    public Barraca salvar(Barraca barraca) {
        barraca.setId(proximoId++);
        cache.add(barraca);
        salvarNoArquivo();
        return barraca;
    }

    @Override
    public List<Barraca> listarTodos() {
        return Collections.unmodifiableList(cache);
    }

    @Override
    public Optional<Barraca> buscarPorId(int id) {
        return cache.stream().filter(b -> b.getId() == id).findFirst();
    }

    @Override
    public Optional<Barraca> buscarPorNome(String nome) {
        return cache.stream()
                .filter(b -> b.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }

    @Override
    public void atualizar(Barraca barraca) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId() == barraca.getId()) {
                cache.set(i, barraca);
                break;
            }
        }
        salvarNoArquivo();
    }

    @Override
    public void remover(int id) {
        cache.removeIf(b -> b.getId() == id);
        salvarNoArquivo();
    }

    @Override
    public boolean existePorTipoBarracaId(int tipoBarracaId) {
        return cache.stream().anyMatch(b -> b.getTipoBarracaId() == tipoBarracaId);
    }

    // -------------------------------------------------------------------------
    // Métodos privados de persistência
    // -------------------------------------------------------------------------

    /**
     * Carrega a lista de Barraca do arquivo JSON para o cache em memória.
     * Registros com dados corrompidos são ignorados com aviso no console.
     */
    private void carregarDoArquivo() {
        List<Map<String, String>> lista = JsonMini.lerLista(ARQUIVO);
        for (Map<String, String> mapa : lista) {
            try {
                Barraca b = new Barraca();
                b.setId(Integer.parseInt(mapa.get("id")));
                b.setNome(mapa.get("nome"));
                b.setAtivo(Boolean.parseBoolean(mapa.get("ativo")));
                b.setTipoBarracaId(Integer.parseInt(mapa.get("tipoBarracaId")));
                cache.add(b);
            } catch (Exception e) {
                System.err.println("[AVISO] Registro de Barraca ignorado por dado inválido: " + mapa + " — " + e.getMessage());
            }
        }
    }

    /**
     * Persiste o cache atual de Barracas no arquivo JSON.
     * Chamado após cada operação de escrita.
     */
    private void salvarNoArquivo() {
        List<Map<String, String>> lista = new ArrayList<>();
        for (Barraca b : cache) {
            Map<String, String> mapa = new LinkedHashMap<>();
            mapa.put("id", String.valueOf(b.getId()));
            mapa.put("nome", b.getNome());
            mapa.put("ativo", String.valueOf(b.isAtivo()));
            mapa.put("tipoBarracaId", String.valueOf(b.getTipoBarracaId()));
            lista.add(mapa);
        }
        JsonMini.escreverLista(ARQUIVO, lista);
    }
}
