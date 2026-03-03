package com.reciclagem.jogo.controller;

import com.reciclagem.jogo.model.Item;
import com.reciclagem.jogo.model.Lixeira;
import com.reciclagem.jogo.service.JogoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class JogoApiController {

    private final JogoService jogoService;

    public JogoApiController() {
        this.jogoService = new JogoService();
    }

    @GetMapping("/itens")
    public List<Item> getItens() {
        return jogoService.getItensDisponiveis();
    }

    @GetMapping("/lixeiras")
    public List<Lixeira> getLixeiras() {
        return jogoService.getLixeiras();
    }

    @GetMapping("/pontuacao")
    public Map<String, Integer> getPontuacao() {
        Map<String, Integer> resposta = new HashMap<>();
        resposta.put("pontuacao", jogoService.getPontuacao());
        return resposta;
    }

    @PostMapping("/jogar")
    public ResponseEntity<Map<String, Object>> jogar(@RequestBody JogadaRequest request) {
        boolean acertou = jogoService.validarJogada(
                request.getItemId(),
                request.getCorLixeira()
        );

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("acertou", acertou);
        resposta.put("pontuacao", jogoService.getPontuacao());

        Item item = jogoService.getItemPorId(request.getItemId());
        Lixeira lixeira = jogoService.getLixeiraPorCor(request.getCorLixeira());

        String mensagem;
        if (acertou) {
            mensagem = String.format("🎉 Muito bem! %s vai na lixeira %s! +10 pontos",
                    item.getNome(), lixeira.getNome());
        } else {
            mensagem = String.format("😢 Ops! %s não vai na lixeira %s. Tente novamente!",
                    item.getNome(), lixeira.getNome());
        }
        resposta.put("mensagem", mensagem);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/resetar")
    public ResponseEntity<Map<String, String>> resetar() {
        jogoService.resetPontuacao();
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Jogo reiniciado! Pontuação zerada.");
        return ResponseEntity.ok(resposta);
    }
}

class JogadaRequest {
    private int itemId;
    private String corLixeira;

    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public String getCorLixeira() { return corLixeira; }
    public void setCorLixeira(String corLixeira) { this.corLixeira = corLixeira; }
}