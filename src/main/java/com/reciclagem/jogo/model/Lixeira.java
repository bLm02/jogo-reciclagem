package com.reciclagem.jogo.model;

public class Lixeira {
    private String cor;
    private String categoriaAceita;
    private String nome; // "Plástico", "Papel", etc.

    public Lixeira() {
    }

    public Lixeira(String cor, String categoriaAceita, String nome) {
        this.cor = cor;
        this.categoriaAceita = categoriaAceita;
        this.nome = nome;
    }

    public boolean aceitaItem(Item item) {
        return this.categoriaAceita.equalsIgnoreCase(item.getCategoria());
    }

    // Getters e Setters
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCategoriaAceita() {
        return categoriaAceita;
    }

    public void setCategoriaAceita(String categoriaAceita) {
        this.categoriaAceita = categoriaAceita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Lixeira{" +
                "cor='" + cor + '\'' +
                ", categoriaAceita='" + categoriaAceita + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}