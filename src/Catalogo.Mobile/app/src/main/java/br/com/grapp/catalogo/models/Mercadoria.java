package br.com.grapp.catalogo.models;

/**
 * Classe padrão JavaBean com seus atributos (privador) e métods gets e setters
 */
public class Mercadoria {
    private int _id;

    private String _nome;
    private double _preco;

    public Mercadoria(int ID, String nome, double preco) {
        _id = ID;
        _nome = nome;
        _preco = preco;
    }

    public int getID() {
        return _id;
    }

    public void setID(int ID) {
        _id = ID;
    }

    public String getNome() {
        return _nome;
    }

    public void setNome(String nome) {
        _nome = nome;
    }

    public double getPreco() {
        return _preco;
    }

    public void setPreco(double preco) {
        _preco = preco;
    }
}
