package aplicacao;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        int option, specificGame;
        Scanner in = new Scanner(System.in);
        File arq = new File("C:\\Users\\gugui\\OneDrive\\Área de Trabalho\\quake_input2.log");
        FileReader fr = new FileReader(arq);
        Parser p = new Parser(fr);
        p.quakeParser();
        do {
            menu();
            option = in.nextInt();
            in.nextLine();
            while (option < 1 || option > 3) {
                System.out.println("Escolha uma opção válida");
                menu();
                option = in.nextInt();
                in.nextLine();
            }
            switch (option) {
                case 1:
                    p.allGamesInfo();
                    break;
                case 2:
                    System.out.println("Informe o endereço do game que queira as informações");
                    specificGame = in.nextInt();
                    in.nextLine();
                    p.selectInfoGame(specificGame);
                    break;
                case 3:
                    System.out.println("Aplicação encerrada");
                    break;
            }
        } while (option != 3);

    }

    public static void menu() {
        System.out.print("""
                         O que deseja fazer?
                         1 - Exibir a informação de todos os jogos
                         2 - Exibir a informação de um jogo específico
                         3 - encerrar o programa
                         """);
    }
}
