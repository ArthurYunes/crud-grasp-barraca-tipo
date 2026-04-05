package feira.graspcrud.controller;

import feira.graspcrud.domain.Barraca;
import feira.graspcrud.domain.TipoBarraca;
import feira.graspcrud.dto.BarracaRequest;
import feira.graspcrud.dto.TipoBarracaRequest;
import feira.graspcrud.exception.RegraNegocioException;
import feira.graspcrud.service.BarracaService;
import feira.graspcrud.service.TipoBarracaService;

import java.util.List;
import java.util.Scanner;

/**
 * Controller responsável por receber as escolhas do menu textual e delegar
 * as operações aos serviços de domínio.
 *
 * <p>Padrão GRASP: Controller — ponto de entrada único para as interações do
 * usuário via terminal. Não implementa regras de negócio; apenas lê a entrada,
 * chama o serviço adequado e exibe o resultado ou a mensagem de erro.
 *
 * <p>Padrão GRASP: High Cohesion — trata exclusivamente da interação
 * com o usuário (entrada/saída), sem misturar lógica de domínio ou persistência.
 */
public class BarracaController {

    private final BarracaService barracaService;
    private final TipoBarracaService tipoBarracaService;
    private final Scanner scanner;

    /**
     * Constrói o controller com os serviços necessários e o leitor de entrada.
     *
     * @param barracaService     serviço de casos de uso de Barraca
     * @param tipoBarracaService serviço de casos de uso de TipoBarraca
     * @param scanner            leitor de entrada do terminal
     */
    public BarracaController(BarracaService barracaService, TipoBarracaService tipoBarracaService, Scanner scanner) {
        this.barracaService = barracaService;
        this.tipoBarracaService = tipoBarracaService;
        this.scanner = scanner;
    }

    /**
     * Inicia o loop principal do menu textual.
     * Permanece ativo até que o usuário escolha a opção de sair.
     */
    public void iniciar() {
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerInt();
            switch (opcao) {
                case 1:
                    menuTipoBarraca();
                    break;
                case 2:
                    menuBarraca();
                    break;
                case 0:
                    System.out.println("\nSaindo do sistema. Até logo!");
                    break;
                default:
                    System.out.println("[!] Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);
    }

    // =========================================================================
    // Menu TipoBarraca
    // =========================================================================

    /**
     * Exibe e processa o submenu de TipoBarraca.
     */
    private void menuTipoBarraca() {
        int opcao;
        do {
            System.out.println("\n=== MENU - TIPO DE BARRACA ===");
            System.out.println("1. Cadastrar TipoBarraca");
            System.out.println("2. Listar TipoBarracas");
            System.out.println("3. Remover TipoBarraca");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1:
                    cadastrarTipoBarraca();
                    break;
                case 2:
                    listarTipoBarracas();
                    break;
                case 3:
                    removerTipoBarraca();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("[!] Opção inválida.");
                    break;
            }
        } while (opcao != 0);
    }

    /**
     * Lê os dados do terminal e delega ao serviço o cadastro de um TipoBarraca.
     */
    private void cadastrarTipoBarraca() {
        System.out.println("\n-- Cadastrar TipoBarraca --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();
        try {
            TipoBarraca tipo = tipoBarracaService.cadastrar(new TipoBarracaRequest(nome));
            System.out.println("[OK] TipoBarraca cadastrado: " + tipo);
        } catch (RegraNegocioException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    /**
     * Solicita ao serviço a lista de TipoBarracas e exibe no terminal.
     */
    private void listarTipoBarracas() {
        List<TipoBarraca> tipos = tipoBarracaService.listar();
        System.out.println("\n-- Tipos de Barraca cadastrados --");
        if (tipos.isEmpty()) {
            System.out.println("Nenhum TipoBarraca cadastrado.");
        } else {
            tipos.forEach(System.out::println);
        }
    }

    /**
     * Lê o id do terminal e delega ao serviço a remoção do TipoBarraca,
     * respeitando a regra de não remover tipo em uso por barracas.
     */
    private void removerTipoBarraca() {
        listarTipoBarracas();
        System.out.print("\nInforme o ID do TipoBarraca a remover: ");
        int id = lerInt();
        try {
            tipoBarracaService.remover(id);
            System.out.println("[OK] TipoBarraca removido com sucesso.");
        } catch (RegraNegocioException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    // =========================================================================
    // Menu Barraca
    // =========================================================================

    /**
     * Exibe e processa o submenu de Barraca.
     */
    private void menuBarraca() {
        int opcao;
        do {
            System.out.println("\n=== MENU - BARRACA ===");
            System.out.println("1. Cadastrar Barraca");
            System.out.println("2. Listar Barracas Ativas");
            System.out.println("3. Listar Todas as Barracas");
            System.out.println("4. Buscar Barraca por ID");
            System.out.println("5. Atualizar Barraca");
            System.out.println("6. Remover Barraca");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = lerInt();
            switch (opcao) {
                case 1:
                    cadastrarBarraca();
                    break;
                case 2:
                    listarBarracasAtivas();
                    break;
                case 3:
                    listarTodasBarracas();
                    break;
                case 4:
                    buscarBarracaPorId();
                    break;
                case 5:
                    atualizarBarraca();
                    break;
                case 6:
                    removerBarraca();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("[!] Opção inválida.");
                    break;
            }
        } while (opcao != 0);
    }

    /**
     * Lê os dados do terminal e delega ao serviço o cadastro de uma Barraca.
     * Exibe aviso e aborta se não houver TipoBarraca cadastrado.
     */
    private void cadastrarBarraca() {
        System.out.println("\n-- Cadastrar Barraca --");
        listarTipoBarracas();
        if (tipoBarracaService.listar().isEmpty()) {
            System.out.println("[AVISO] Cadastre ao menos um TipoBarraca antes de cadastrar uma Barraca.");
            return;
        }
        System.out.print("Nome da Barraca: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Ativa? (s/n): ");
        boolean ativo = scanner.nextLine().trim().equalsIgnoreCase("s");
        System.out.print("ID do TipoBarraca: ");
        int tipoId = lerInt();
        try {
            Barraca b = barracaService.cadastrar(new BarracaRequest(nome, ativo, tipoId));
            System.out.println("[OK] Barraca cadastrada: " + formatarBarraca(b));
        } catch (RegraNegocioException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    /**
     * Solicita ao serviço a lista de barracas ativas e exibe no terminal.
     */
    private void listarBarracasAtivas() {
        List<Barraca> barracas = barracaService.listarAtivas();
        System.out.println("\n-- Barracas Ativas --");
        if (barracas.isEmpty()) {
            System.out.println("Nenhuma barraca ativa encontrada.");
        } else {
            barracas.forEach(b -> System.out.println(formatarBarraca(b)));
        }
    }

    /**
     * Solicita ao serviço a lista completa de barracas e exibe no terminal.
     */
    private void listarTodasBarracas() {
        List<Barraca> barracas = barracaService.listarTodas();
        System.out.println("\n-- Todas as Barracas (incluindo inativas) --");
        if (barracas.isEmpty()) {
            System.out.println("Nenhuma barraca cadastrada.");
        } else {
            barracas.forEach(b -> System.out.println(formatarBarraca(b)));
        }
    }

    /**
     * Lê o id do terminal, solicita busca ao serviço e exibe a Barraca encontrada.
     */
    private void buscarBarracaPorId() {
        System.out.print("\nInforme o ID da Barraca: ");
        int id = lerInt();
        try {
            Barraca b = barracaService.buscarPorId(id);
            System.out.println(formatarBarraca(b));
        } catch (RegraNegocioException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    /**
     * Lê os dados atualizados do terminal e delega ao serviço a atualização da Barraca.
     */
    private void atualizarBarraca() {
        System.out.print("\nInforme o ID da Barraca a atualizar: ");
        int id = lerInt();
        try {
            Barraca atual = barracaService.buscarPorId(id);
            System.out.println("Dados atuais: " + formatarBarraca(atual));
            listarTipoBarracas();
            System.out.print("Novo nome [" + atual.getNome() + "]: ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) nome = atual.getNome();
            System.out.print("Ativa? (s/n) [" + (atual.isAtivo() ? "s" : "n") + "]: ");
            String ativoStr = scanner.nextLine().trim();
            boolean ativo = ativoStr.isEmpty() ? atual.isAtivo() : ativoStr.equalsIgnoreCase("s");
            System.out.print("ID do TipoBarraca [" + atual.getTipoBarracaId() + "]: ");
            String tipoStr = scanner.nextLine().trim();
            int tipoId = tipoStr.isEmpty() ? atual.getTipoBarracaId() : Integer.parseInt(tipoStr);
            Barraca atualizada = barracaService.atualizar(id, new BarracaRequest(nome, ativo, tipoId));
            System.out.println("[OK] Barraca atualizada: " + formatarBarraca(atualizada));
        } catch (RegraNegocioException e) {
            System.out.println("[ERRO] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("[ERRO] ID inválido informado.");
        }
    }

    /**
     * Lê o id do terminal e delega ao serviço a remoção da Barraca.
     */
    private void removerBarraca() {
        System.out.print("\nInforme o ID da Barraca a remover: ");
        int id = lerInt();
        try {
            barracaService.remover(id);
            System.out.println("[OK] Barraca removida com sucesso.");
        } catch (RegraNegocioException e) {
            System.out.println("[ERRO] " + e.getMessage());
        }
    }

    // =========================================================================
    // Métodos auxiliares
    // =========================================================================

    /**
     * Exibe o menu principal no terminal.
     */
    private void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║  SISTEMA DE GESTÃO - FEIRA LIVRE   ║");
        System.out.println("║       Barraca / TipoBarraca         ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║  1. Gerenciar TipoBarraca           ║");
        System.out.println("║  2. Gerenciar Barraca               ║");
        System.out.println("║  0. Sair                            ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.print("Opção: ");
    }

    /**
     * Formata uma Barraca para exibição no terminal, resolvendo o nome do TipoBarraca.
     *
     * @param b a barraca a formatar
     * @return string formatada com dados da barraca e nome do tipo
     */
    private String formatarBarraca(Barraca b) {
        String nomeType;
        try {
            nomeType = tipoBarracaService.buscarPorId(b.getTipoBarracaId()).getNome();
        } catch (RegraNegocioException e) {
            nomeType = "Desconhecido";
        }
        return String.format("[%d] %s | Ativo: %s | Tipo: %s",
                b.getId(), b.getNome(), b.isAtivo() ? "Sim" : "Não", nomeType);
    }

    /**
     * Lê um inteiro do terminal, consumindo a linha restante.
     * Retorna -1 em caso de entrada inválida.
     *
     * @return inteiro lido ou -1 se inválido
     */
    private int lerInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
