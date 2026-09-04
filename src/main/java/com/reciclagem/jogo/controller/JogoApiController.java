package com.reciclagem.jogo.controller;

import com.reciclagem.jogo.dto.IniciarJogoRequest;
import com.reciclagem.jogo.dto.IniciarJogoResponse;
import com.reciclagem.jogo.dto.JogadaRequest;
import com.reciclagem.jogo.dto.ResultadoJogada;
import com.reciclagem.jogo.dto.Rodada;
import com.reciclagem.jogo.model.Item;
import com.reciclagem.jogo.model.Lixeira;
import com.reciclagem.jogo.service.JogoService;
import com.reciclagem.jogo.service.SessaoNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class JogoApiController {

    private final JogoService jogoService;

    public JogoApiController(JogoService jogoService) {
        this.jogoService = jogoService;
    }

    @GetMapping("/itens")
    public List<Item> getItens() {
        return jogoService.getItensDisponiveis();
    }

    @GetMapping("/lixeiras")
    public List<Lixeira> getLixeiras() {
        return jogoService.getLixeiras();
    }

    @PostMapping("/jogo/iniciar")
    public ResponseEntity<IniciarJogoResponse> iniciarJogo(@RequestBody IniciarJogoRequest request) {
        if (request.getNomeJogador() == null || request.getNomeJogador().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(jogoService.iniciarJogo(request.getNomeJogador().trim()));
    }

    @PostMapping("/jogo/{sessionId}/jogada")
    public ResponseEntity<ResultadoJogada> jogar(@PathVariable String sessionId, @RequestBody JogadaRequest request) {
        return ResponseEntity.ok(jogoService.processarJogada(sessionId, request.getItemId()));
    }

    @PostMapping("/jogo/{sessionId}/proxima-rodada")
    public ResponseEntity<Rodada> proximaRodada(@PathVariable String sessionId) {
        return ResponseEntity.ok(jogoService.avancarRodada(sessionId));
    }

    @ExceptionHandler(SessaoNaoEncontradaException.class)
    public ResponseEntity<String> tratarSessaoNaoEncontrada(SessaoNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
