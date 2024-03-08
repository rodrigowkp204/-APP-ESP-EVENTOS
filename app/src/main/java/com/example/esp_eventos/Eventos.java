package com.example.esp_eventos;

import com.google.firebase.Timestamp;

public class Eventos {

    private Timestamp dataAdicao;
    private String eventosId;
    private String datadoEvento;
    private String datadoEventoFinal;
    private String descricaodaAtividade;
    private String horadoEventoInicial;
    private String horaEventoFinal;
    private String localdoEvento;
    private String nomedoEvento;
    private String observacoes;
    private String numerosdeParticipantes;
    private String setorResponsavel;
    private String equipamentos;
    private String presencial;
    private String online;
    private String hibrido;
    private String linkTransmissao;
    private String quaisequipamentos;
    private String temAmbosTipo;
    private String temGravacaoTipo;
    private String temtransmissao;
    private String temTransmissaoTipo;
    private String materiaisgraficos;
    private String coberturafotografica;
    private String userId;

    public Eventos(String eventosId,String datadoEvento, String datadoEventoFinal ,
                   String descricaodaAtividade, String horadoEventoInicial, String horaEventoFinal,
                   String localdoEvento, String nomedoEvento,String observacoes,
                   String numerosdeParticipantes, String setorResponsavel,
                   String equipamentos,String presencial, String online,String hibrido,String linkTransmissao,
                   String quaisequipamentos,String temAmbosTipo,
                   String temGravacaoTipo,String temtransmissao,String temTransmissaoTipo,
                   String materiaisgraficos, String coberturafotografica, String userId){
        this.eventosId = eventosId;
        this.datadoEvento = datadoEvento;
        this.datadoEventoFinal = datadoEventoFinal;
        this.descricaodaAtividade = descricaodaAtividade;
        this.horadoEventoInicial = horadoEventoInicial;
        this.horaEventoFinal = horaEventoFinal;
        this.localdoEvento = localdoEvento;
        this.nomedoEvento = nomedoEvento;
        this.observacoes = observacoes;
        this.numerosdeParticipantes = numerosdeParticipantes;
        this.setorResponsavel = setorResponsavel;
        this.equipamentos = equipamentos;
        this.presencial = presencial;
        this.online = online;
        this.hibrido = hibrido;
        this.linkTransmissao = linkTransmissao;
        this.quaisequipamentos = quaisequipamentos;
        this.temAmbosTipo = temAmbosTipo;
        this.temGravacaoTipo = temGravacaoTipo;
        this.temtransmissao = temtransmissao;
        this.temTransmissaoTipo = temTransmissaoTipo;
        this.materiaisgraficos = materiaisgraficos;
        this.coberturafotografica = coberturafotografica;
        this.userId = userId;
    }

    public Eventos (){
    }
    public Timestamp getDataAdicao() {
        return dataAdicao;
    }
    public void setDataAdicao(Timestamp dataAdicao) {
        this.dataAdicao = dataAdicao;
    }

    public String geteventosId() {
        return eventosId;
    }
    public void setEventosId(String eventosId) {
        this.eventosId = eventosId;
    }
    public String getdatadoEvento() {
        return datadoEvento;
    }
    public void setdatadoEvento(String datadoEvento) {
        this.datadoEvento = datadoEvento;
    }
    public String getDatadoEventoFinal() {
        return datadoEventoFinal;
    }
    public void setDatadoEventoFinal(String datadoEventoFinal) {
        this.datadoEventoFinal = datadoEventoFinal;
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
    public String getHoraEventoFinal() {
        return horaEventoFinal;
    }
    public void setHoraEventoFinal(String horaEventoFinal) {
        this.horaEventoFinal = horaEventoFinal;
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

    public String getObservacoes() {
        return observacoes;
    }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getNumerosdeParticipantes() {
        return numerosdeParticipantes;
    }
    public void setNumerosdeParticipantes(String numerosdeParticipantes) {
        this.numerosdeParticipantes = numerosdeParticipantes;
    }
    public String getSetorResponsavel() {
        return setorResponsavel;
    }
    public void setSetorResponsavel(String setorResponsavel) {
        this.setorResponsavel = setorResponsavel;
    }

    public String getEquipamentos(){
        return equipamentos;
    }
    public void setEquipamentos(String equipamentos){
        this.equipamentos = equipamentos;
    }

    public String getPresencial(){
        return presencial;
    }
    public void setPresencial(String presencial){
        this.presencial = presencial;
    }

    public String getOnline(){
        return online;
    }
    public void setOnline(String online){
        this.online = online;
    }

    public String getHibrido(){
        return hibrido;
    }
    public void setHibrido(String hibrido){
        this.hibrido = hibrido;
    }

    public String getLinkTransmissao(){
        return linkTransmissao;
    }
    public void setLinkTransmissao(String linkTransmissao){
        this.linkTransmissao = linkTransmissao;
    }

    public String getQuaisequipamentos(){
        return quaisequipamentos;
    }
    public void setQuaisequipamentos(String quaisequipamentos){
        this.quaisequipamentos = quaisequipamentos;
    }

    public String getTemtransmissao(){
        return temtransmissao;
    }
    public void setTemtransmissao(String temtransmissao){
        this.temtransmissao = temtransmissao;
    }
    public String getTemAmbosTipo(){
        return temAmbosTipo;
    }
    public void setTemAmbosTipo(String temAmbosTipo){
        this.temAmbosTipo = temAmbosTipo;
    }

    public String getTemGravacaoTipo(){
        return temGravacaoTipo;
    }
    public void setTemGravacaoTipo(String temGravacaoTipo){
        this.temGravacaoTipo = temGravacaoTipo;
    }

    public String getTemTransmissaoTipo(){
        return temTransmissaoTipo;
    }
    public void setTemTransmissaoTipo(String temTransmissaoTipo){
        this.temTransmissaoTipo = temTransmissaoTipo;
    }

    public String getMateriaisgraficos(){
        return materiaisgraficos;
    }
    public void setMateriaisgraficos(String materiaisgraficos){
        this.materiaisgraficos = materiaisgraficos;
    }

    public String getCoberturafotografica() {
        return coberturafotografica;
    }
    public void setCoberturafotografica(String coberturafotografica) {
        this.coberturafotografica = coberturafotografica;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
