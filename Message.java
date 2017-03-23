import javax.json.*;
import java.io.*;

public class Message {

    private int msgId;
    private long date;      //nb de ms depuis le 01 01 1970
    private JsonObject content;

    public Message(){
        msgId = 0;
        date =  System.currentTimeMillis();
        content = null;
    }

    public Message(int id, JsonObject j){
        msgId = id;
        date =  System.currentTimeMillis();
        content = j;
    }

    public void setId(int id){
        msgId = id;
    }

    public void setDate(){
        date = System.currentTimeMillis();
    }

    public void setDateWithValue(long d){
        date = d;
    }

    public void setContent(JsonObject obj){
        content = obj;
    }

    public int getMsgId(){
        return  msgId;
    }

    public long getDate(){
        return date;
    }

    public JsonObject getContent(){
        return content;
    }

    public String toString(){
        StringBuffer s = new StringBuffer("msg_id : " + getMsgId() + "\n")
                .append("date : " + getDate() + "\n")
                .append("contents : " + getContent() + "\n");
        return new String(s);
    }

    public JsonObject toCompleteJsonObject(){
        JsonObject obj = Json.createObjectBuilder()
                .add("mdg_id", getMsgId())
                .add("date", getDate())
                .add("contents", getContent())
                .build();
        return obj;
    }

}
