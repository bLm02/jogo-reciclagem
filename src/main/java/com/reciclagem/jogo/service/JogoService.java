package com.reciclagem.jogo.service;

import com.reciclagem.jogo.dto.IniciarJogoResponse;
import com.reciclagem.jogo.dto.ResultadoJogada;
import com.reciclagem.jogo.dto.Rodada;
import com.reciclagem.jogo.model.Item;
import com.reciclagem.jogo.model.Lixeira;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class JogoService {

    private static final int TOTAL_RODADAS = 10;
    private static final int OPCOES_POR_RODADA = 4;
    private static final int[] VALORES_PONTUACAO = {10, 5, 2, 0};

    private final List<Item> itens;
    private final List<Lixeira> lixeiras;
    private final Map<String, SessaoJogo> sessoes = new ConcurrentHashMap<>();

    public JogoService() {
        this.itens = criarItens();
        this.lixeiras = criarLixeiras();
    }

    public List<Item> getItensDisponiveis() {
        return new ArrayList<>(itens);
    }

    public List<Lixeira> getLixeiras() {
        return new ArrayList<>(lixeiras);
    }

    public IniciarJogoResponse iniciarJogo(String nomeJogador) {
        String sessionId = UUID.randomUUID().toString();
        SessaoJogo sessao = new SessaoJogo(nomeJogador, gerarRodadas());
        sessoes.put(sessionId, sessao);
        return new IniciarJogoResponse(sessionId, montarRodada(sessao));
    }

    public ResultadoJogada processarJogada(String sessionId, int itemId) {
        SessaoJogo sessao = obterSessao(sessionId);
        RodadaInterna rodadaAtual = sessao.rodadaAtual();

        Item itemEscolhido = rodadaAtual.opcoes.stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElse(null);

        boolean acertou = itemEscolhido != null && rodadaAtual.lixeira.aceitaItem(itemEscolhido);

        if (acertou) {
            int pontosGanhos = pontosParaTentativa(sessao.tentativas);
            sessao.pontuacao += pontosGanhos;

            boolean ultimaRodada = sessao.indiceRodada == sessao.rodadas.size() - 1;
            if (ultimaRodada) {
                String mensagemFinal = gerarMensagemFinal(sessao.nomeJogador, sessao.pontuacao);
                sessoes.remove(sessionId);
                return new ResultadoJogada(true, pontosGanhos, sessao.pontuacao,
                        sessao.tentativas + 1, 0, true, mensagemFinal);
            }

            return new ResultadoJogada(true, pontosGanhos, sessao.pontuacao,
                    sessao.tentativas + 1, pontosParaTentativa(sessao.tentativas), false, null);
        }

        sessao.tentativas++;
        return new ResultadoJogada(false, 0, sessao.pontuacao,
                sessao.tentativas + 1, pontosParaTentativa(sessao.tentativas), false, null);
    }

    public Rodada avancarRodada(String sessionId) {
        SessaoJogo sessao = obterSessao(sessionId);
        sessao.indiceRodada++;
        sessao.tentativas = 0;
        return montarRodada(sessao);
    }

    private SessaoJogo obterSessao(String sessionId) {
        SessaoJogo sessao = sessoes.get(sessionId);
        if (sessao == null) {
            throw new SessaoNaoEncontradaException(sessionId);
        }
        return sessao;
    }

    private int pontosParaTentativa(int indiceTentativa) {
        return indiceTentativa < VALORES_PONTUACAO.length ? VALORES_PONTUACAO[indiceTentativa] : 0;
    }

    private Rodada montarRodada(SessaoJogo sessao) {
        RodadaInterna rodadaInterna = sessao.rodadaAtual();
        return new Rodada(
                sessao.indiceRodada + 1,
                sessao.rodadas.size(),
                rodadaInterna.lixeira,
                rodadaInterna.opcoes,
                sessao.tentativas + 1,
                pontosParaTentativa(sessao.tentativas)
        );
    }

    private List<RodadaInterna> gerarRodadas() {
        List<RodadaInterna> combinacoes = new ArrayList<>();
        for (Lixeira lixeira : lixeiras) {
            for (Item item : itens) {
                if (lixeira.aceitaItem(item)) {
                    combinacoes.add(new RodadaInterna(lixeira, item, itens));
                }
            }
        }
        Collections.shuffle(combinacoes);
        return new ArrayList<>(combinacoes.subList(0, Math.min(TOTAL_RODADAS, combinacoes.size())));
    }

    private String gerarMensagemFinal(String nomeJogador, int pontuacao) {
        double nota = pontuacao / (double) TOTAL_RODADAS;
        if (nota <= 5) {
            return "Poxa " + nomeJogador + ", você precisa treinar mais um pouco! ♻️";
        } else if (nota <= 7) {
            return "Muito bem, " + nomeJogador + "! Você foi ok e já sabe bastante! 👍";
        } else if (nota <= 9) {
            return "Parabéns, " + nomeJogador + "! Você foi ótimo e é um craque da reciclagem! 🌟";
        }
        return "Incrível, " + nomeJogador + "! Você fez a pontuação perfeita! Você é um mestre ambiental! 🏆";
    }

    private List<Item> criarItens() {
        List<Item> lista = new ArrayList<>();

        // === PLÁSTICO (5 itens) - Lixeira VERMELHA ===
        lista.add(new Item(1, "Garrafa PET", "plastico", "/images/lixos/Plastico/Garrafa PET.png"));
        lista.add(new Item(2, "Pote de iogurte", "plastico", "/images/lixos/Plastico/Pote de iogurte.png"));
        lista.add(new Item(3, "Sacola plástica", "plastico", "/images/lixos/Plastico/Sacola plastica.png"));
        lista.add(new Item(4, "Tampa de garrafa", "plastico", "/images/lixos/Plastico/Tampa de garrafa.png"));
        lista.add(new Item(5, "Copo descartável", "plastico", "/images/lixos/Plastico/Copo descartável.png"));

        // === PAPEL (5 itens) - Lixeira AZUL ===
        lista.add(new Item(6, "Jornal", "papel", "/images/lixos/Papel/Jornal.png"));
        lista.add(new Item(7, "Caixa de papelão", "papel", "/images/lixos/Papel/Caixa de papelão.png"));
        lista.add(new Item(8, "Revista", "papel", "/images/lixos/Papel/Revista.png"));
        lista.add(new Item(9, "Rolo de papel higiênico", "papel", "/images/lixos/Papel/Rolo de papel higiênico.png"));
        lista.add(new Item(10, "Caixa de leite", "papel", "/images/lixos/Papel/Caixa de leite.png"));

        // === METAL (5 itens) - Lixeira AMARELA ===
        lista.add(new Item(11, "Lata de refrigerante", "metal", "/images/lixos/Metal/Lata de refrigerante.png"));
        lista.add(new Item(12, "Lata de atum", "metal", "/images/lixos/Metal/Lata de atum.png"));
        lista.add(new Item(13, "Papel alumínio", "metal", "/images/lixos/Metal/_Papel alumínio.png"));
        lista.add(new Item(14, "Fio de cobre", "metal", "/images/lixos/Metal/Fio de cobre.png"));
        lista.add(new Item(15, "Tampa de metal", "metal", "/images/lixos/Metal/Tampa de metal.png"));

        // === VIDRO (5 itens) - Lixeira VERDE ===
        lista.add(new Item(16, "Garrafa de vidro", "vidro", "/images/lixos/Vidro/Garrafa de vidro.png"));
        lista.add(new Item(17, "Pote de vidro", "vidro", "/images/lixos/Vidro/Pote de vidro.png"));
        lista.add(new Item(18, "Jarra de vidro", "vidro", "/images/lixos/Vidro/Jarra de vidro.png"));
        lista.add(new Item(19, "Caco de vidro", "vidro", "/images/lixos/Vidro/Caco de vidro.png"));
        lista.add(new Item(20, "Pote de geleia", "vidro", "/images/lixos/Vidro/Pote de geleia.png"));

        // === ORGÂNICO (5 itens) - Lixeira MARROM ===
        lista.add(new Item(21, "Casca de banana", "organico", "/images/lixos/Organico/casca de banana.png"));
        lista.add(new Item(22, "Casca de laranja", "organico", "/images/lixos/Organico/casca de laranja.png"));
        lista.add(new Item(23, "Saquinho de chá", "organico", "/images/lixos/Organico/Saquinho de chá.png"));
        lista.add(new Item(24, "Casca de ovo", "organico", "/images/lixos/Organico/casca de ovo.png"));
        lista.add(new Item(25, "Resto de comida", "organico", "/images/lixos/Organico/resto de comida.png"));

        return lista;
    }

    private List<Lixeira> criarLixeiras() {
        List<Lixeira> lista = new ArrayList<>();
        lista.add(new Lixeira("azul", "papel", "Papel", "/images/lixeiras/Papel.jpeg"));
        lista.add(new Lixeira("vermelho", "plastico", "Plástico", "/images/lixeiras/Plástico.jpeg"));
        lista.add(new Lixeira("verde", "vidro", "Vidro", "/images/lixeiras/Vidro.jpeg"));
        lista.add(new Lixeira("amarelo", "metal", "Metal", "/images/lixeiras/Metal.jpeg"));
        lista.add(new Lixeira("marrom", "organico", "Orgânico", "/images/lixeiras/Orgânico.jpeg"));
        return lista;
    }

    private static class SessaoJogo {
        final String nomeJogador;
        final List<RodadaInterna> rodadas;
        int indiceRodada = 0;
        int tentativas = 0;
        int pontuacao = 0;

        SessaoJogo(String nomeJogador, List<RodadaInterna> rodadas) {
            this.nomeJogador = nomeJogador;
            this.rodadas = rodadas;
        }

        RodadaInterna rodadaAtual() {
            return rodadas.get(indiceRodada);
        }
    }

    private static class RodadaInterna {
        final Lixeira lixeira;
        final List<Item> opcoes;

        RodadaInterna(Lixeira lixeira, Item itemCorreto, List<Item> todosItens) {
            this.lixeira = lixeira;
            this.opcoes = montarOpcoes(lixeira, itemCorreto, todosItens);
        }

        private static List<Item> montarOpcoes(Lixeira lixeira, Item itemCorreto, List<Item> todosItens) {
            List<Item> incorretos = todosItens.stream()
                    .filter(item -> !lixeira.aceitaItem(item))
                    .collect(Collectors.toList());
            Collections.shuffle(incorretos);

            List<Item> opcoes = new ArrayList<>();
            opcoes.add(itemCorreto);
            opcoes.addAll(incorretos.subList(0, Math.min(OPCOES_POR_RODADA - 1, incorretos.size())));
            Collections.shuffle(opcoes);
            return opcoes;
        }
    }
}
