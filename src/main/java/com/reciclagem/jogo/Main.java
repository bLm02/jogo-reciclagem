package com.reciclagem.jogo;

import com.reciclagem.jogo.service.JogoService;
import com.reciclagem.jogo.model.Item;
import com.reciclagem.jogo.model.Lixeira;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Jogo Reciclagem\n");

        JogoService jogo = new JogoService();
        Scanner scanner = new Scanner(System.in);

        // Mostrar itens disponíveis
        System.out.println("Itens para reciclar:");
        for (Item item : jogo.getItensDisponiveis()) {
            System.out.println("  " + item.getId() + " - " + item.getNome() +
                    " (Categoria: " + item.getCategoria() + ")");
        }

        // Mostrar lixeiras
        System.out.println("\nLixeiras disponíveis:");
        for (Lixeira lixeira : jogo.getLixeiras()) {
            System.out.println("  Cor: " + lixeira.getCor() +
                    " - Aceita: " + lixeira.getCategoriaAceita() +
                    " (" + lixeira.getNome() + ")");
        }

        // Menu interativo simples
        boolean jogando = true;
        while (jogando) {
            System.out.println("\n--- MENU ---");
            System.out.println("1 - Jogar");
            System.out.println("2 - Ver pontuação");
            System.out.println("3 - Sair");
            System.out.print("Escolha: ");

            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Digite o ID do item: ");
                    int itemId = scanner.nextInt();
                    System.out.print("Digite a cor da lixeira: ");
                    String cor = scanner.next();

                    boolean acertou = jogo.validarJogada(itemId, cor);

                    if (acertou) {
                        System.out.println("Parabéns! Você acertou!");
                    } else {
                        System.out.println("Tente novamente!");
                    }
                    break;

                case 2:
                    System.out.println("Pontuação atual: " + jogo.getPontuacao());
                    break;

                case 3:
                    jogando = false;
                    System.out.println("Jogo encerrado! Pontuação final: " + jogo.getPontuacao());
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }

        scanner.close();
    }
}