import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Bus {

    private int incId;
    private ArrayList<Capteur> capteurList;

    public Bus(){
        incId = 0;
        capteurList = new ArrayList<Capteur>();
    }

    public void incrementId(){
        incId++;
    }

    public JsonObject generalHandler(JsonObject request){

        JsonObject answer = Json.createObjectBuilder().build();

        //Si la requete est de type "register"
        if(request.getString("type").equals("register")){
            String classe = request.getString("sender_class");
            String name = request.getString("sender_name");
            Capteur c = new Capteur(classe, name);
            answer = registerHandler(c);
        }
        //Si la requete est de type "deregister"
        else if(request.getString("type").equals("deregister")){
            int senderId = request.getInt("sender_id");
            answer = deregisterHandler(senderId);
        }
        //Si la requete est de type "list"
        //On traite le format de la requete, avec ou sans argument
        else if(request.getString("type").equals("list")){
            if(request.getString("sender_class").equals(null) && request.getString("sender_name").equals(null)){
                answer = listHandler();
            }
            else if(!request.getString("sender_name").equals(null)){
                String name = request.getString("sender_name");
                answer = listHandlerViaClass(name);
            }
            else if(!request.getString("sender_class").equals(null)){
                String classe = request.getString("sender_class");
                answer = listHandlerViaClass(classe);
            }
            else{
                answer = Json.createObjectBuilder()
                        .add("type", "list")
                        .add("ack", errorHandler(404))
                        .build();
            }
        }
        //Si la requete est de type "send"
        else if(request.getString("type").equals("send")){
            int id = request.getInt("sender_id");
            answer = sendHandler(request, id);
        }
        else if(request.getString("type").equals("get")){
            int senderId = request.getInt("sender_id");
            int msgId = request.getInt("msg_id");
            answer = getMessageHandler(senderId, msgId);
        }
        else if(request.getString("type").equals("get_last")){
            int senderId = request.getInt("sender_id");
            answer = getLastMessageHandler(senderId);
        }

        return answer;
    }

    public JsonObject registerHandler(Capteur c){
        incrementId();
        c.setId(incId);
        capteurList.add(c);
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "register")
                .add("sender_id", incId)
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok")
                ).build();
        return answer;
    }

    public JsonObject deregisterHandler(int i){
        JsonObject answer;
        boolean isRemoved = false;
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            if(it.next().getId() == i){
                it.remove();
                isRemoved = true;
            }
        }
        //Si l'objet n'a pas été remove on renvoie un code d'erreur
        if(!isRemoved){
            answer = Json.createObjectBuilder()
                    .add("type", "deregister")
                    .add("ack", errorHandler(404)
                    ).build();
        }
        else{
            answer = Json.createObjectBuilder()
                    .add("type", "deregister")
                    .add("ack", Json.createObjectBuilder()
                            .add("resp", "ok")
                    ).build();
        }
        return answer;
    }

    public JsonObject listHandler(){
        JsonArrayBuilder b = Json.createArrayBuilder();
        StringBuffer str =  new StringBuffer("");
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            b.add(it.next().toJson());
        }
        JsonArray ar = b.build();
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                    .add("resp", "ok"))
                .add("results", ar)
                .build();
        return answer;
    }


    public JsonObject listHandlerViaClass(String c){
        JsonArrayBuilder b = Json.createArrayBuilder();
        StringBuffer str =  new StringBuffer("");
        Capteur capteurTmp;
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            capteurTmp = it.next();
            if(capteurTmp.getClasse().equals(c)) {
                b.add(capteurTmp.toJson());
            }
        }
        JsonArray ar = b.build();
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok"))
                .add("results", ar)
                .build();
        return answer;
    }

    public JsonObject listHandlerViaName(String n){
        JsonArrayBuilder builder = Json.createArrayBuilder();
        StringBuffer str =  new StringBuffer("");
        Capteur capteurTmp;
        for(Iterator<Capteur> it = capteurList.iterator(); it.hasNext();){
            capteurTmp = it.next();
            if(capteurTmp.getName().equals(n)) {
                builder.add(capteurTmp.toJson());
            }
        }
        JsonArray ar = builder.build();
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "list")
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok"))
                .add("results", ar)
                .build();
        return answer;
    }

    public JsonObject sendHandler(JsonObject request, int id){
        for(Capteur c : capteurList){
            if(c.getId() == id){
                JsonObject content = Json.createObjectBuilder()
                        .add("contents", request.getJsonObject("contents"))
                        .build();
                c.addMessage(new Message(
                        c.getCurrentMsgId(),
                        content
                ));
            }
        }
        JsonObject answer = Json.createObjectBuilder()
                .add("type", "send")
                .add("ack", Json.createObjectBuilder()
                        .add("resp", "ok"))
                .build();
        return answer;
    }

    public JsonObject getMessageHandler(int senderId, int msgId){
        Message msg = new Message();
        JsonObject answer;
        for(Capteur c : capteurList){
            if(c.getId() == senderId){
                msg = c.getMessage(msgId);
                break;
            }
        }
        if(msg == null){
            answer = Json.createObjectBuilder()
                    .add("type", "get")
                    .add("ack", errorHandler(404))
                    .build();
        }
        else{
            answer = Json.createObjectBuilder()
                    .add("type", "get")
                    .add("ack", Json.createObjectBuilder()
                            .add("resp", "ok"))
                    .add("msg_id", msg.getMsgId())
                    .add("date", msg.getDate())
                    .add("contents", msg.getContent().getJsonObject("contents"))
                    .build();
        }
        return  answer;
    }

    public JsonObject getLastMessageHandler(int senderId){
        Message msg = new Message();
        JsonObject answer = Json.createObjectBuilder().build();
        for(Capteur c : capteurList){
            if(c.getId() == senderId){
                msg = c.getLastMessage();
                break;
            }
        }
        if(msg == null){
            answer = Json.createObjectBuilder()
                    .add("type", "get")
                    .add("ack", errorHandler(404))
                    .build();
        }
        else{
            answer = Json.createObjectBuilder()
                    .add("type", "get")
                    .add("ack", Json.createObjectBuilder()
                            .add("resp", "ok"))
                    .add("msg_id", msg.getMsgId())
                    .add("date", msg.getDate())
                    .add("contents", msg.getContent().getJsonObject("contents"))
                    .build();
        }
        return  answer;
    }

    public JsonObject errorHandler(int errorId){
        JsonObject obj = Json.createObjectBuilder()
                .add("resp","error")
                .add("error_id", errorId)
                .build();
        return obj;
    }

}
