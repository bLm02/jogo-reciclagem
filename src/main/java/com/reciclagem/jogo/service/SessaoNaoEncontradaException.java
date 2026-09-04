package com.reciclagem.jogo.service;

public class SessaoNaoEncontradaException extends RuntimeException {
    public SessaoNaoEncontradaException(String sessionId) {
        super("Sessão de jogo não encontrada: " + sessionId);
    }
}
