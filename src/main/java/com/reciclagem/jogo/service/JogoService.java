package com.reciclagem.jogo.service;

import com.reciclagem.jogo.model.Item;
import com.reciclagem.jogo.model.Lixeira;
import java.util.ArrayList;
import java.util.List;

public class JogoService {
    private List<Item> itens;
    private List<Lixeira> lixeiras;
    private int pontuacao;

    public JogoService() {
        this.itens = new ArrayList<>();
        this.lixeiras = new ArrayList<>();
        this.pontuacao = 0;
        inicializarJogo();
    }

    private void inicializarJogo() {
        // Cadastrar itens
        itens.add(new Item(1, "Garrafa PET", "vermelho", "plastico"));
        itens.add(new Item(2, "Pote de iogurte", "vermelho", "plastico"));
        itens.add(new Item(3, "Sacola plástica", "vermelho", "plastico"));
        itens.add(new Item(4, "Tampa de garrafa", "vermelho", "plastico"));
        itens.add(new Item(5, "Copo descartável", "vermelho", "plastico"));

        // === PAPEL (5 itens) - Lixeira AZUL ===
        itens.add(new Item(6, "Jornal", "azul", "papel"));
        itens.add(new Item(7, "Caixa de papelão", "azul", "papel"));
        itens.add(new Item(8, "Revista", "azul", "papel"));
        itens.add(new Item(9, "Rolo de papel higiênico", "azul", "papel"));
        itens.add(new Item(10, "Caixa de leite", "azul", "papel"));

        // === METAL (5 itens) - Lixeira AMARELA ===
        itens.add(new Item(11, "Lata de refrigerante", "amarelo", "metal"));
        itens.add(new Item(12, "Lata de atum", "amarelo", "metal"));
        itens.add(new Item(13, "Papel alumínio", "amarelo", "metal"));
        itens.add(new Item(14, "Fio de cobre", "amarelo", "metal"));
        itens.add(new Item(15, "Tampa de metal", "amarelo", "metal"));

        // === VIDRO (5 itens) - Lixeira VERDE ===
        itens.add(new Item(16, "Garrafa de vidro", "verde", "vidro"));
        itens.add(new Item(17, "Pote de vidro", "verde", "vidro"));
        itens.add(new Item(18, "Garrafa de cerveja", "verde", "vidro"));
        itens.add(new Item(19, "Caco de vidro", "verde", "vidro"));
        itens.add(new Item(20, "Pote de geleia", "verde", "vidro"));

        // === ORGÂNICO (5 itens) - Lixeira MARROM ===
        itens.add(new Item(21, "Casca de banana", "marrom", "organico"));
        itens.add(new Item(22, "Casca de laranja", "marrom", "organico"));
        itens.add(new Item(23, "Saquinho de chá", "marrom", "organico"));
        itens.add(new Item(24, "Casca de ovo", "marrom", "organico"));
        itens.add(new Item(25, "Resto de comida", "marrom", "organico"));

        // Cadastrar lixeiras (cores padrão da reciclagem)
        lixeiras.add(new Lixeira("azul", "papel", "Papel"));
        lixeiras.add(new Lixeira("vermelho", "plastico", "Plástico"));
        lixeiras.add(new Lixeira("verde", "vidro", "Vidro"));
        lixeiras.add(new Lixeira("amarelo", "metal", "Metal"));
        lixeiras.add(new Lixeira("marrom", "organico", "Orgânico"));
    }

    public boolean validarJogada(int itemId, String corLixeira) {

        // 1. Buscar o item pelo ID
        Item item = getItemPorId(itemId);

        // 2. Buscar a lixeira pela cor
        Lixeira lixeira = getLixeiraPorCor(corLixeira);

        // 3. Verificar se o item e a lixeira existem
        if (item == null || lixeira == null) {
            System.out.println("Item ou lixeira não encontrados!");
            return false;
        }

        // 4. Verificar se a lixeira aceita o item
        boolean acertou = lixeira.aceitaItem(item);

        // 5. Se acertou, aumentar pontuação
        if (acertou) {
            pontuacao += 10;
            System.out.println("+10 pontos!");
        } else {
            System.out.println("Esse item não vai nessa lixeira!");
        }

        return acertou;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void resetPontuacao() {
        this.pontuacao = 0;
    }

    public List<Item> getItensDisponiveis() {
        return new ArrayList<>(itens);
    }

    public List<Lixeira> getLixeiras() {
        return new ArrayList<>(lixeiras);
    }

    public Item getItemPorId(int id) {
        for (Item item : itens) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public Lixeira getLixeiraPorCor(String cor) {
        for (Lixeira lixeira : lixeiras) {
            if (lixeira.getCor().equalsIgnoreCase(cor)) {
                return lixeira;
            }
        }
        return null;
    }
}