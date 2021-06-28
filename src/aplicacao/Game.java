package aplicacao;

import java.util.ArrayList;

public class Game {

    private final int id;
    private int killCount;
    private ArrayList<Player> players;

    public Game(int id) {
        this.id = id;
        this.killCount = 0;
        this.players = new ArrayList<>();
    }

    public Game(int id, int killCount, ArrayList<Player> players) {
        this.id = id;
        this.killCount = killCount;
        this.players = players;
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public int searchPlayer(Player p) {
        return players.indexOf(p);
    }

    public boolean alreadyHasThisPlayer(Player p) {
        int i;
        for (i = 0; i < players.size(); i = i + 1) {
            if (players.get(i).equals(p) == true) {
                return true;
            }
        }
        return false;
    }

    public void removePlayerByHisID(int idRemove) {
        Player p;
        int i;
        for (i = 0; i < players.size(); i = i + 1) {
            if (players.get(i).getId() == idRemove) {
                p = players.get(i);
                this.players.remove(p);
                return;
            }
        }
    }

    public int searchPlayer(int searchedIdPlayer) {
        int i;
        for (i = 0; i < players.size(); i = i + 1) {
            if (players.get(i).getId() == searchedIdPlayer) { // retorna o index do player procurado
                return i;
            }
        }
        return -1; //caso o player procurado não esteja no jogo, retorne -1
    }

    public int getKillCount() {
        return this.killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public int getID() {
        return id;
    }

    public void setTotalKills() {
        int i, total = 0;
        for (i = 0; i < players.size(); i = i + 1) {
            total = total + players.get(i).getPersonalKillCount();
        }
        this.setKillCount(total);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void increaseKillCountToPlayer(int id) {
        int i;
        for (i = 0; i < players.size(); i = i + 1) {
            if (players.get(i).getId() == id) {
                players.get(i).increasedPersonalKillCount();
            }
        }
    }

    public void reduceKillCountToPlayer(int id) {
        int i;
        for (i = 0; i < players.size(); i = i + 1) {
            if (players.get(i).getId() == id) {
                players.get(i).reducePersonalKillCount();
            }
        }
    }

    public String gameInfoByID(int i) {
        String totalKills = "Total de kills: " + this.killCount + ';';
        String totalPlayers = getAllPlayers();
        String totalPlayersAndKills = getAllPlayersWithTheirKills();
        return "game_" + i + "_info: {\n"
                + '\t' + totalKills + '\n'
                + '\t' + "players: [" + totalPlayers + "]\n"
                + '\t' + "kills dos players: {\n"
                + '\t' + '\t' + totalPlayersAndKills + '\n'
                + '\t' + "}\n}";
    }

    @Override
    public String toString() {
        String totalKills = "Total de kills: " + this.killCount + ';';
        String totalPlayers = getAllPlayers();
        String totalPlayersAndKills = getAllPlayersWithTheirKills();
        return "game_" + this.id + "_info: {\n"
                + '\t' + totalKills + '\n'
                + '\t' + "players: [" + totalPlayers + "]\n"
                + '\t' + "kills dos players: {\n"
                + '\t' + '\t' + totalPlayersAndKills + '\n'
                + '\t' + "}\n}";
    }

    private String getAllPlayers() {
        if (players.isEmpty() == false) {
            String result = players.get(0).getNickname();
            for (int i = 1; i < players.size(); i = i + 1) {
                result = result + ',' + players.get(i).getNickname();
            }
            return result;
        }
        return null;
    }

    private String getAllPlayersWithTheirKills() {
        if (players.isEmpty() == false) {
            String result = players.get(0).toString();
            for (int i = 1; i < players.size(); i = i + 1) {
                result = result + ",\n\t\t" + players.get(i).toString();
            }
            return result;
        }
        return null;
    }

}
