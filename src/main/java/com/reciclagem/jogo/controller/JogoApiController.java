package com.reciclagem.jogo.controller;

import com.reciclagem.jogo.model.Item;
import com.reciclagem.jogo.model.Lixeira;
import com.reciclagem.jogo.service.JogoService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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
}