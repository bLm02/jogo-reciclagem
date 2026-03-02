package com.reciclagem.jogo.model;

public class Item {
    private int id;
    private String nome;
    private String cor;
    private String categoria; // plastico, papel, vidro, metal, organico

    public Item(int id, String nome, String cor, String categoria) {
        this.id = id;
        this.nome = nome;
        this.cor = cor;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cor='" + cor + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}