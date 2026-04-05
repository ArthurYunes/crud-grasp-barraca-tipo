package feira.graspcrud.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Utilitário de leitura e escrita JSON em Java puro, sem bibliotecas externas.
 *
 * <p>Padrão GRASP: Pure Fabrication — esta classe não representa nenhum conceito
 * do domínio da feira livre; foi fabricada exclusivamente para isolar os detalhes
 * técnicos de serialização/desserialização JSON, mantendo as entidades e serviços
 * livres de preocupações de infraestrutura.
 *
 * <p>Suporta objetos simples com campos do tipo String, int e boolean.
 * O formato gerado é compatível com qualquer parser JSON padrão.
 */
public class JsonMini {

    /**
     * Lê um arquivo JSON e retorna uma lista de mapas, cada mapa representando um objeto.
     *
     * <p>Retorna lista vazia se o arquivo não existir ou estiver vazio.
     *
     * @param caminho caminho do arquivo JSON a ser lido
     * @return lista de mapas com os campos de cada objeto
     */
    public static List<Map<String, String>> lerLista(String caminho) {
        List<Map<String, String>> resultado = new ArrayList<>();
        File arquivo = new File(caminho);
        if (!arquivo.exists()) return resultado;

        try {
            String conteudo = new String(Files.readAllBytes(Paths.get(caminho)), StandardCharsets.UTF_8).trim();
            if (conteudo.isEmpty() || conteudo.equals("[]")) return resultado;

            // Remove colchetes externos
            conteudo = conteudo.substring(1, conteudo.length() - 1).trim();

            // Divide objetos separados por },{
            List<String> objetos = dividirObjetos(conteudo);
            for (String obj : objetos) {
                resultado.add(parsearObjeto(obj));
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo JSON: " + caminho);
        }
        return resultado;
    }

    /**
     * Escreve uma lista de mapas como array JSON no arquivo informado.
     *
     * <p>Cria o arquivo e os diretórios necessários caso não existam.
     * Sobrescreve o conteúdo anterior do arquivo.
     *
     * @param caminho caminho do arquivo JSON a ser escrito
     * @param lista   lista de mapas a serem serializados
     */
    public static void escreverLista(String caminho, List<Map<String, String>> lista) {
        try {
            File arquivo = new File(caminho);
            arquivo.getParentFile().mkdirs();
            StringBuilder sb = new StringBuilder("[\n");
            for (int i = 0; i < lista.size(); i++) {
                sb.append("  ").append(serializarObjeto(lista.get(i)));
                if (i < lista.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("]");
            Files.write(Paths.get(caminho), sb.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo JSON: " + caminho);
        }
    }

    // -------------------------------------------------------------------------
    // Métodos auxiliares privados
    // -------------------------------------------------------------------------

    /**
     * Divide o conteúdo JSON (sem colchetes externos) em strings de objetos individuais.
     *
     * @param conteudo string contendo objetos JSON separados por vírgula
     * @return lista de strings, cada uma representando um objeto JSON
     */
    private static List<String> dividirObjetos(String conteudo) {
        List<String> objetos = new ArrayList<>();
        int profundidade = 0;
        int inicio = 0;
        for (int i = 0; i < conteudo.length(); i++) {
            char c = conteudo.charAt(i);
            if (c == '{') profundidade++;
            else if (c == '}') {
                profundidade--;
                if (profundidade == 0) {
                    objetos.add(conteudo.substring(inicio, i + 1).trim());
                    inicio = i + 2; // pula vírgula e espaço
                }
            }
        }
        return objetos;
    }

    /**
     * Converte uma string JSON de objeto simples em mapa chave→valor (tudo como String).
     *
     * @param json string JSON representando um objeto
     * @return mapa com os pares chave-valor extraídos
     */
    private static Map<String, String> parsearObjeto(String json) {
        Map<String, String> mapa = new LinkedHashMap<>();
        // Remove { e }
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);
        json = json.trim();

        // Extrai pares chave:valor
        int i = 0;
        while (i < json.length()) {
            // Pula espaços e vírgulas
            while (i < json.length() && (json.charAt(i) == ',' || json.charAt(i) == ' ' || json.charAt(i) == '\n' || json.charAt(i) == '\r' || json.charAt(i) == '\t')) i++;
            if (i >= json.length()) break;

            // Lê chave
            String chave = lerString(json, i);
            i += chave.length() + 2; // +2 para as aspas
            while (i < json.length() && json.charAt(i) != ':') i++;
            i++; // pula ':'
            while (i < json.length() && json.charAt(i) == ' ') i++;

            // Lê valor (string ou literal)
            String valor;
            if (json.charAt(i) == '"') {
                valor = lerString(json, i);
                i += valor.length() + 2;
            } else {
                int fim = i;
                while (fim < json.length() && json.charAt(fim) != ',' && json.charAt(fim) != '}') fim++;
                valor = json.substring(i, fim).trim();
                i = fim;
            }
            mapa.put(chave, valor);
        }
        return mapa;
    }

    /**
     * Lê uma string JSON com aspas a partir de uma posição, retornando apenas o conteúdo interno.
     *
     * @param json string JSON completa
     * @param pos  posição inicial (deve ser uma aspas dupla)
     * @return conteúdo da string sem aspas
     */
    private static String lerString(String json, int pos) {
        StringBuilder sb = new StringBuilder();
        int i = pos + 1; // pula aspas abertura
        while (i < json.length() && json.charAt(i) != '"') {
            if (json.charAt(i) == '\\') {
                i++; // pula o caractere de escape
                if (i >= json.length()) break; // proteção contra escape no fim da string
            }
            sb.append(json.charAt(i));
            i++;
        }
        return sb.toString();
    }

    /**
     * Serializa um mapa de campos para uma string JSON de objeto.
     *
     * @param mapa mapa com pares chave-valor a serializar
     * @return string JSON representando o objeto
     */
    private static String serializarObjeto(Map<String, String> mapa) {
        StringBuilder sb = new StringBuilder("{");
        List<String> chaves = new ArrayList<>(mapa.keySet());
        for (int i = 0; i < chaves.size(); i++) {
            String chave = chaves.get(i);
            String valor = mapa.get(chave);
            sb.append("\"").append(chave).append("\": ");
            // Detecta se é número ou boolean
            if (valor.equals("true") || valor.equals("false") || valor.matches("-?\\d+(\\.\\d+)?")) {
                sb.append(valor);
            } else {
                sb.append("\"").append(escapar(valor)).append("\"");
            }
            if (i < chaves.size() - 1) sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Escapa caracteres especiais em uma string para uso em JSON.
     *
     * @param valor string a ser escapada
     * @return string com caracteres especiais substituídos por sequências de escape JSON
     */
    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("\"", "\\\"")
                    .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }
}
