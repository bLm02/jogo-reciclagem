package com.reciclagem.jogo.dto;

public class IniciarJogoResponse {
    private final String sessionId;
    private final Rodada rodada;

    public IniciarJogoResponse(String sessionId, Rodada rodada) {
        this.sessionId = sessionId;
        this.rodada = rodada;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Rodada getRodada() {
        return rodada;
    }
}
