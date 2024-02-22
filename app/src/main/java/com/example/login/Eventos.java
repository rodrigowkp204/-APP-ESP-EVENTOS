package com.example.login;

public class Eventos {

    //private String Observacoes;
    private String datadoEvento;
    private String descricaodaAtividade;
    private String horadoEventoInicial;
    private String localdoEvento;
    private String nomedoEvento;


    public Eventos() {

    }

    public Eventos(String datadoEvento, String descricaodaAtividade, String horadoEventoInicial, String localdoEvento, String nomedoEvento){
        this.datadoEvento = datadoEvento;
        this.descricaodaAtividade = descricaodaAtividade;
        this.horadoEventoInicial = horadoEventoInicial;
        this.localdoEvento = localdoEvento;
        this.nomedoEvento = nomedoEvento;
    }

    public String getdatadoEvento() {
        return datadoEvento;
    }

    public void setdatadoEvento(String datadoEvento) {
        this.datadoEvento = datadoEvento;
    }

    public String getdescricaodaAtividade() {
        return descricaodaAtividade;
    }

    public void setdescricaodaAtividade(String descricaodaAtividade) {
        this.descricaodaAtividade = descricaodaAtividade;
    }
    public String getHoradoEventoInicial() {
        return horadoEventoInicial;
    }

    public void setHoradoEventoInicial(String horadoEventoInicial) {
        this.horadoEventoInicial = horadoEventoInicial;
    }

    public String getlocaldoEvento() {
        return localdoEvento;
    }

    public void setlocaldoEvento(String localdoEvento) {
        this.localdoEvento = localdoEvento;
    }

    public String getnomedoEvento() {
        return nomedoEvento;
    }

    public void setnomedoEvento(String nomedoEvento) {
        this.nomedoEvento = nomedoEvento;
    }

}
