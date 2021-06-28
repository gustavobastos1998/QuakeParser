package aplicacao;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    private int gameIndex;
    private BufferedReader br;
    private String fileLine;
    private String buffer;
    private ArrayList<Game> allGames;

    public Parser(FileReader arq) throws FileNotFoundException, IOException {
        gameIndex = 0;
        br = new BufferedReader(arq);
        fileLine = pickNextImportantLine(br);
        buffer = pickFirstWord(fileLine);
        allGames = new ArrayList<>();
    }

    public void quakeParser() {
        try {
            Game novoJogo;
            Player player;
            ArrayList<Player> allPlayers;
            if (buffer.equals("InitGame") == true) {
                novoJogo = new Game(gameIndex);
                increaseGameIndex();
                allPlayers = new ArrayList<>();
                while (fileLine.equals("application is over") == false) {
                    fileLine = pickNextImportantLine(br);
                    buffer = pickFirstWord(fileLine);
                    switch (buffer) {
                        case "ClientUserinfoChanged":
                            int idPlayer;
                            int searchedPlayerIndex;
                            String playerName;
                            boolean hasThisPlayer;
                            if (novoJogo.isEmpty() != false) { // se nao tiver player no jogo
                                idPlayer = getPlayerID(fileLine);
                                playerName = getPlayerName(fileLine);
                                player = new Player(playerName, idPlayer);
                                allPlayers.add(player);
                                novoJogo.addPlayer(player);
                            } else {
                                idPlayer = getPlayerID(fileLine);
                                searchedPlayerIndex = novoJogo.searchPlayer(idPlayer);
                                playerName = getPlayerName(fileLine);
                                if (searchedPlayerIndex == -1) { // se nao existir o player no jogo, insira
                                    player = new Player(playerName, idPlayer);
                                    hasThisPlayer = novoJogo.alreadyHasThisPlayer(player);
                                    if (hasThisPlayer == false) {
                                        allPlayers.add(player);
                                        novoJogo.addPlayer(player);
                                    }
                                } else { //se nao...
                                    if (novoJogo.getPlayers().get(searchedPlayerIndex).getNickname().equals(playerName) == false) { // se o nick mudou, atualize
                                        novoJogo.getPlayers().get(searchedPlayerIndex).setNickname(playerName);
                                        allPlayers.get(searchedPlayerIndex).setNickname(playerName);
                                    }
                                }
                            }
                            break;
                        case "Kill":
                            int killer;
                            int murdered;
                            killer = getIDKiller(fileLine);
                            if (killer == 1022) { //se o mundo matou, -1 para quem morreu
                                murdered = getIDMurdered(fileLine);
                                novoJogo.reduceKillCountToPlayer(murdered);
                            } else {
                                novoJogo.increaseKillCountToPlayer(killer);
                            }
                            break;
                        case "ShutdownGame":
                            novoJogo.setTotalKills();
                            allGames.add(novoJogo);
                            break;
                        case "ClientConnect":
                            int idConnectedPlayer,
                             searchResult;
                            Player p;
                            idConnectedPlayer = getIDFromClientConnect(fileLine);
                            if (allPlayers.isEmpty() == false) {
                                p = searchPlayerThatEntered(allPlayers, idConnectedPlayer); // procura se o player ja esteve na partida antes
                                searchResult = allPlayers.indexOf(p); // -1: caso nunca esteve na partida | um numero natural: caso ja esteve
                                if (searchResult != -1) {
                                    searchResult = novoJogo.searchPlayer(idConnectedPlayer); // -1 caso o player se disconectou, porem ja esteve na partida e um numero natural caso ele nao tenha se desconectado
                                    if (searchResult == -1) { // player esteve no jogo, disconectou e reconectou agora
                                        novoJogo.addPlayer(p); // insere o player de novo ao jogo, com as kills zeradas
                                    }
                                }
                            }
                            break;
                        case "ClientDisconnect":
                            int idDisconnectedPlayer;
                            idDisconnectedPlayer = getIDFromClientDisconnect(fileLine);
                            novoJogo.removePlayerByHisID(idDisconnectedPlayer);
                            break;
                        case "InitGame":
                            quakeParser();
                            break;
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

    }

    public void allGamesInfo() {
        int i;
        for (i = 0; i < allGames.size(); i = i + 1) {
            System.out.println(allGames.get(i));
        }
    }

    public void selectInfoGame(int i) {
        try {
            System.out.println(allGames.get(i).gameInfoByID(i));
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println("Informaçoes de jogo nao encontradas, endereço fora dos limites");
        }
    }

    private Player searchPlayerThatEntered(ArrayList<Player> p, int id) {
        Player result;
        int i;
        for (i = 0; i < p.size(); i = i + 1) {
            if (p.get(i).getId() == id) {
                result = p.get(i);
                return result;
            }
        }
        return null;
    }

    private int getIDFromClientConnect(String line) {
        int i, result;
        String aux = "";
        for (i = 15; i < line.length(); i = i + 1) {
            if (Character.isDigit(line.charAt(i)) == true) {
                aux = aux + line.charAt(i);
            }
        }
        result = Integer.parseInt(aux);
        return result;
    }

    private int getIDFromClientDisconnect(String line) {
        int i, result;
        String aux = "";
        for (i = 18; i < line.length(); i = i + 1) {
            if (Character.isDigit(line.charAt(i)) == true) {
                aux = aux + line.charAt(i);
            }
        }
        result = Integer.parseInt(aux);
        return result;
    }

    private int getIDKiller(String line) {
        int i, result;
        String aux = "";
        for (i = 6;; i = i + 1) {
            if (Character.isDigit(line.charAt(i)) == true) {
                aux = aux + line.charAt(i);
            } else if (Character.isWhitespace(line.charAt(i)) == true) {
                result = Integer.parseInt(aux);
                return result;
            }
        }
    }

    private int getIDMurdered(String line) {
        int i, result;
        String aux = "";
        for (i = 11;; i = i + 1) {
            if (Character.isDigit(line.charAt(i)) == true) {
                aux = aux + line.charAt(i);
            } else if (Character.isWhitespace(line.charAt(i)) == true) {
                result = Integer.parseInt(aux);
                return result;
            }
        }
    }

    private void increaseGameIndex() {
        gameIndex = gameIndex + 1;
    }

    private int getPlayerID(String line) {
        int i;
        String id = "";
        for (i = 23; Character.isDigit(line.charAt(i)) == true; i = i + 1) {// enquanto o caracter atual for diferente de "\", adicione os caracteres ao nome
            id = id + line.charAt(i);
        }
        return Integer.parseInt(id);//pega o valor da string e converte para Integer
    }

    private String getPlayerName(String line) {
        int i;
        String name = "";
        for (i = 27; line.charAt(i) != '\\'; i = i + 1) { // enquanto o caracter atual for diferente de "\", adicione os caracteres ao nome
            name = name + line.charAt(i);
        }
        return name;
    }

    private String pickNextImportantLine(BufferedReader br) throws IOException {
        String line;
        String aux;
        line = fileLine;
        line = cleanLine(line);
        aux = pickFirstWord(line);
        while (aux.equals("InitGame") == false
                && aux.equals("ClientUserinfoChanged") == false
                && aux.equals("Kill") == false
                && aux.equals("ShutdownGame") == false
                && aux.equals("ClientConnect") == false
                && aux.equals("ClientDisconnect") == false) { // enquanto nao for uma linha que comece por initgame, clientuserinfo etc, leio a proxima linha e testo novamente
            line = br.readLine();
            if (line == null) {
                return "application is over";
            }
            line = cleanLine(line);
            aux = pickFirstWord(line);
        }
        return line;
    }

    private String cleanLine(String s) {
        if (s != null) {
            s = s.subSequence(7, s.length()).toString();
        }
        return s;
    }

    private String pickFirstWord(String s) {
        String result = "";
        if (s != null) {
            char c;
            for (int i = 0; i < s.length(); i++) {
                c = s.charAt(i);
                if (c == ':' || c == '-') {
                    return result;
                }
                result = result + c;
            }
        }
        return result;
    }
}
