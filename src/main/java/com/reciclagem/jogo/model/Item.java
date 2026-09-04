package com.reciclagem.jogo.model;

public class Item {
    private int id;
    private String nome;
    private String categoria; // plastico, papel, vidro, metal, organico
    private String imagemUrl;

    public Item(int id, String nome, String categoria, String imagemUrl) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.imagemUrl = imagemUrl;
    }

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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", imagemUrl='" + imagemUrl + '\'' +
                '}';
    }
}
