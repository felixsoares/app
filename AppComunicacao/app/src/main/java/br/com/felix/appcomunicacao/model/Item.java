package br.com.felix.appcomunicacao.model;

public class Item {

    private String nome;
    private Integer valor;

    public Item(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }
}
