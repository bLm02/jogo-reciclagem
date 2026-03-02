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
        itens.add(new Item(1, "Garrafa PET", "azul", "plastico"));
        itens.add(new Item(2, "Jornal", "amarelo", "papel"));
        itens.add(new Item(3, "Lata de refrigerante", "vermelho", "metal"));
        itens.add(new Item(4, "Garrafa de vidro", "verde", "vidro"));
        itens.add(new Item(5, "Casca de banana", "marrom", "organico"));

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