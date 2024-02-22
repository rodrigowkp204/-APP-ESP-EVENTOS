package com.example.esp_eventos;

import com.google.firebase.Timestamp;

public class EventosExterno {

    private String eventosExtId, nomedoEventoExterno, localEventoExterno,
            horaInicialEventoExterno,horaFinalEventoExterno,
            descricaoEventoExterno, dataInicialEventoExterno,
            dataFinalEventoExterno, observacoesEventoExterno,
            participantesEventoExterno, setorResponsavelEventoExterno;
    private String equipamentosEventoExterno;
    private String presencialEventoExterno;
    private String onlineEventoExterno;
    private String hibridoEventoExterno;
    private String linkTransmissaoEventoExterno;
    private String quaisequipamentosEventoExterno;
    private String temAmbosTipoEventoExterno;
    private String temGravacaoTipoEventoExterno;
    private String temtransmissaoEventoExterno;
    private String temTransmissaoTipoEventoExterno;
    private String materiaisgraficosEventoExterno;
    private Timestamp dataAdicaoext;

    public EventosExterno(){}

    public EventosExterno(String eventosExtId, String nomedoEventoExterno, String localEventoExterno, String horaInicialEventoExterno, String horaFinalEventoExterno, String descricaoEventoExterno, String dataInicialEventoExterno, String dataFinalEventoExterno, String observacoesEventoExterno, String participantesEventoExterno, String setorResponsavelEventoExterno,
                          String equipamentosEventoExterno,String presencialEventoExterno,String onlineEventoExterno,String hibridoEventoExterno,String linkTransmissaoEventoExterno,String quaisequipamentosEventoExterno,String temAmbosTipoEventoExterno,String temGravacaoTipoEventoExterno,String temtransmissaoEventoExterno,String temTransmissaoTipoEventoExterno,String materiaisgraficosEventoExterno){
        this.eventosExtId = eventosExtId;
        this.nomedoEventoExterno = nomedoEventoExterno;
        this.localEventoExterno = localEventoExterno;
        this.horaInicialEventoExterno = horaInicialEventoExterno;
        this.horaFinalEventoExterno = horaFinalEventoExterno;
        this.descricaoEventoExterno = descricaoEventoExterno;
        this.dataInicialEventoExterno = dataInicialEventoExterno;
        this.dataFinalEventoExterno = dataFinalEventoExterno;
        this.observacoesEventoExterno = observacoesEventoExterno;
        this.participantesEventoExterno = participantesEventoExterno;
        this.setorResponsavelEventoExterno = setorResponsavelEventoExterno;
        this.equipamentosEventoExterno = equipamentosEventoExterno;
        this.presencialEventoExterno = presencialEventoExterno;
        this.onlineEventoExterno = onlineEventoExterno;
        this.hibridoEventoExterno = hibridoEventoExterno;
        this.linkTransmissaoEventoExterno = linkTransmissaoEventoExterno;
        this.quaisequipamentosEventoExterno = quaisequipamentosEventoExterno;
        this.temAmbosTipoEventoExterno = temAmbosTipoEventoExterno;
        this.temGravacaoTipoEventoExterno = temGravacaoTipoEventoExterno;
        this.temtransmissaoEventoExterno = temtransmissaoEventoExterno;
        this.temTransmissaoTipoEventoExterno = temTransmissaoTipoEventoExterno;
        this.materiaisgraficosEventoExterno = materiaisgraficosEventoExterno;
    }

    public Timestamp getDataAdicaoext() {
        return dataAdicaoext;
    }
    public void setDataAdicaoext(Timestamp dataAdicaoext) {
        this.dataAdicaoext = dataAdicaoext;
    }

    public String geteventosExtId() {
        return eventosExtId;
    }
    public void setEventosId(String eventosExtId) {
        this.eventosExtId = eventosExtId;
    }
    public String getNomedoEventoExterno() {
        return nomedoEventoExterno;
    }

    public void setNomedoEventoExterno(String nomedoEventoExterno) {
        this.nomedoEventoExterno = nomedoEventoExterno;
    }
    public String getLocalEventoExterno() {
        return localEventoExterno;
    }

    public void setLocalEventoExterno(String localEventoExterno) {
        this.localEventoExterno = localEventoExterno;
    }
    public String getHoraInicialEventoExterno() {
        return horaInicialEventoExterno;
    }

    public void setHoraInicialEventoExterno(String horaInicialEventoExterno) {
        this.horaInicialEventoExterno = horaInicialEventoExterno;
    }
    public String getHoraFinalEventoExterno() {
        return horaFinalEventoExterno;
    }

    public void setHoraFinalEventoExterno(String horaFinalEventoExterno) {
        this.horaFinalEventoExterno = horaFinalEventoExterno;
    }
    public String getDescricaoEventoExterno() {
        return descricaoEventoExterno;
    }

    public void setDescricaoEventoExterno(String descricaoEventoExterno) {
        this.descricaoEventoExterno = descricaoEventoExterno;
    }
    public String getDataInicialEventoExterno() {
        return dataInicialEventoExterno;
    }

    public void setDataInicialEventoExterno(String dataInicialEventoExterno) {
        this.dataInicialEventoExterno = dataInicialEventoExterno;
    }
    public String getDataFinalEventoExterno() {
        return dataFinalEventoExterno;
    }

    public void setDataFinalEventoExterno(String dataFinalEventoExterno) {
        this.dataFinalEventoExterno = dataFinalEventoExterno;
    }

    public String getObservacoesEventoExterno() {
        return observacoesEventoExterno;
    }
    public void setObservacoesEventoExterno(String observacoesEventoExterno) {
        this.observacoesEventoExterno = observacoesEventoExterno;
    }

    public String getParticipantesEventoExterno() {
        return participantesEventoExterno;
    }
    public void setParticipantesEventoExterno(String participantesEventoExterno) {
        this.participantesEventoExterno = participantesEventoExterno;
    }

    public String getSetorResponsavelEventoExterno() {
        return setorResponsavelEventoExterno;
    }
    public void setSetorResponsavelEventoExterno(String setorResponsavelEventoExterno) {
        this.setorResponsavelEventoExterno = setorResponsavelEventoExterno;
    }

    public String getEquipamentosEventoExterno(){
        return equipamentosEventoExterno;
    }
    public void setEquipamentosEventoExterno(String equipamentosEventoExterno){
        this.equipamentosEventoExterno = equipamentosEventoExterno;
    }

    public String getPresencialEventoExterno(){
        return presencialEventoExterno;
    }
    public void setPresencialEventoExterno(String presencialEventoExterno){
        this.presencialEventoExterno = presencialEventoExterno;
    }

    public String getOnlineEventoExterno(){
        return onlineEventoExterno;
    }
    public void setOnlineEventoExterno(String onlineEventoExterno){
        this.onlineEventoExterno = onlineEventoExterno;
    }

    public String getHibridoEventoExterno(){
        return hibridoEventoExterno;
    }
    public void setHibridoEventoExterno(String hibridoEventoExterno){
        this.hibridoEventoExterno = hibridoEventoExterno;
    }

    public String getLinkTransmissaoEventoExterno(){
        return linkTransmissaoEventoExterno;
    }
    public void setLinkTransmissaoEventoExterno(String linkTransmissaoEventoExterno){
        this.linkTransmissaoEventoExterno = linkTransmissaoEventoExterno;
    }

    public String getQuaisequipamentosEventoExterno(){
        return quaisequipamentosEventoExterno;
    }
    public void setQuaisequipamentosEventoExterno(String quaisequipamentosEventoExterno){
        this.quaisequipamentosEventoExterno = quaisequipamentosEventoExterno;
    }

    public String getTemTransmissaoTipoEventoExterno(){
        return temtransmissaoEventoExterno;
    }
    public void setTemtransmissaoEventoExterno(String temtransmissaoEventoExterno){
        this.temtransmissaoEventoExterno = temtransmissaoEventoExterno;
    }

    public String getTemAmbosTipoEventoExterno(){
        return temAmbosTipoEventoExterno;
    }
    public void setTemAmbosTipoEventoExterno(String temAmbosTipoEventoExterno){
        this.temAmbosTipoEventoExterno = temAmbosTipoEventoExterno;
    }

    public String getTemGravacaoTipoEventoExterno(){
        return temGravacaoTipoEventoExterno;
    }
    public void setTemGravacaoTipoEventoExterno(String temGravacaoTipoEventoExterno){
        this.temGravacaoTipoEventoExterno = temGravacaoTipoEventoExterno;
    }

    public String getTemtransmissaoEventoExterno(){
        return temTransmissaoTipoEventoExterno;
    }
    public void setTemTransmissaoTipoEventoExterno(String temTransmissaoTipoEventoExterno){
        this.temTransmissaoTipoEventoExterno = temTransmissaoTipoEventoExterno;
    }

    public String getMateriaisgraficosEventoExterno(){
        return materiaisgraficosEventoExterno;
    }
    public void setMateriaisgraficosEventoExterno(String materiaisgraficosEventoExterno){
        this.materiaisgraficosEventoExterno = materiaisgraficosEventoExterno;
    }
}
