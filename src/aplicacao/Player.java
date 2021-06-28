package aplicacao;

public class Player {
    
    private String nickname;
    private int personalKillCount;
    private int id;
    public Player(String nickname, int id){
        this.nickname = nickname;
        this.personalKillCount = 0;
        this.id = id;
    }
    
    public Player(String nickname, int personalKillCount, int id){
        this.nickname = nickname;
        this.personalKillCount = personalKillCount;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPersonalKillCount() {
        return personalKillCount;
    }

    public void increasedPersonalKillCount() {
        personalKillCount = personalKillCount + 1;
    }
    
    public void reducePersonalKillCount() {
        personalKillCount = personalKillCount - 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        return nickname + ':' +personalKillCount;
    }
}
