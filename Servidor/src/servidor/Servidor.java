/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
*
 */
package servidor;

/**
 *
 * @author regiszanandrea
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * 
 *  Cod	Mensagem			Contexto
    100 Pode continuar                  Quando o ping foi satisfeito.
    200	Ok				Comandos quando responderam corretamente.
    203	Não-Autorizado                  Chave errada na autenticação.
    302 Encontrado                      Arquivo foi encontrado.
    400	Requisição inválida 		Não está no formato ou informação faltando.
    401 Acesso não autorizado           Solicitação por alguém não autenticada.
    404	Arquivo não encontrado          Arquivo não disponível.
    408 Timeout                         Não respondeu a tempo. Obs.: Uso para log.
    501	Não implementado		Ainda falta implementar.


 * 
 * 
 */

public class Servidor {

   public static void main(String[] args) {

       String clienteComando,capitalized = "";
    
       ArrayList<String> listaPeers = new ArrayList<String>();
       //listaPeers.add("");
       ArrayList<String> arquivos = new ArrayList<String>();
       JSONObject my_obj; // Instancia o Objeto Json
       boolean autenticado = false;
       try {
 
           // Cria um SocketServer (Socket característico de um servidor)
           ServerSocket socket = new ServerSocket(9876);
        
           while(true) {    
    
               /* Cria um objeto Socket, mas passando informações características de um servidor,
                *no qual somente abre uma porta e aguarda a conexão de um cliente 
                */
               Socket connectionSocket = socket.accept();
    
               // Cria uma buffer que irá armazenar as informações enviadas pelo cliente
               BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    
               // Cria uma stream de sáida para retorno das informações ao cliente
               DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
    
               // Faz a leitura das informações enviadas pelo cliente as amazenam na variável "clienteComando"
               clienteComando = inFromClient.readLine();
    
               /* Faz uma modificação na String enviada pelo cliente, simulando um processamento em "back-end"
                * antes de retorná-la ao cliente
                */

               //System.out.println(clienteComando);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
               my_obj = new JSONObject(clienteComando);
               //ping
               if(my_obj.getString("command").equals("ping")){ //{protocol:”pcmj”, command:”ping”, sender:”<IP>”,receptor:”<IP>”}
//ARRUMAR AQUI ping
                   //feedback: {protocol:”pcmj”, command:”pong”,status:”<CODIGO>”,  sender:”<IP>”,receptor:”<IP>”}
                    my_obj = new JSONObject();
                    my_obj.put("protocol", "pcmj"); 
                    my_obj.put("command", "pong"); 
                    my_obj.put("status", "100"); 
                    my_obj.put("sender", "localhost");
                    my_obj.put("receptor", "<IP>");
                                       
                    outToClient.writeBytes(my_obj.toString()+"\n"); 
                    //System.out.println(json_string);
                    
                    //connectionSocket.getInetAddress()
                    //connectionSocket.getPort()
                    //connectionSocket.getLocalPort()
                    //connectionSocket.getLocalAddress()
                     
               }else if(my_obj.getString("command").equals("authenticate")){ // {protocol:”pcmj”, command:”authenticate”, passport:”<PASSPORT>, ”sender:”<IP>”, receptor:”<IP>”}
                   
                   //feedback: {protocol:”pcmj”, command:”authenticate-back”,status:”<CODIGO>”,”sender:”<IP>”,receptor:”<IP>”}
                   //falta: 501 como eu coloco o nao implementado?
                   if(my_obj.getString("protocol").equals("pcmj") && my_obj.getString("passport").length() > 0 && my_obj.getString("sender").length() > 0 && my_obj.getString("receptor").length() > 0){
                      my_obj = new JSONObject();
                   
                      my_obj.put("protocol", "pcmj"); 
                      my_obj.put("command", "authenticate-back"); 

                      if(my_obj.getString("passport").equals("DiJqWHqKtiDgZySAv7ZX")){ 
                          my_obj.put("status", "200"); 
                          autenticado = true;
                      }else{
                          my_obj.put("status", "203"); 
                          autenticado = false;
                      }
                      my_obj.put("sender", "localhost");
                      my_obj.put("receptor", "<IP>");
                      outToClient.writeBytes(my_obj.toString()+"\n");  
                   }else{
                        my_obj = new JSONObject();
                        my_obj.put("protocol", "pcmj"); 
                        my_obj.put("command", "authenticate-back"); 
                        my_obj.put("status", "400"); 
                        my_obj.put("sender", "localhost");
                        my_obj.put("receptor", "<IP>");
                        outToClient.writeBytes(my_obj.toString()+"\n");  
                   }
                   
                   
               }
               /* Isso é o servidor principal que fará isso                     
               else if(my_obj.getString("command").equals("agent-list")){
                   //{protocol:”pcmj”, command:”agent-list”, sender:”<IP>”, receptor:”<IP>”}
                   //InetAddress IP=InetAddress.getLocalHost();
                   //System.out.println("IP of my system is := "+IP.getHostAddress());
                   * 
                   * 
                   * 
               }*/
               
               else if(my_obj.getString("command").equals("archive-list")){ // {“protocol”:”pcmj”, ”command”:”archive-list”, “sender”:”<IP>”,”receptor”:”<IP>”}

                   //feedback: {protocol:”pcmj”, command:”archive-list-back”, status:”<CODIGO>”, back:”file:[id:”1”, nome:”file.txt”, size:”200”], folder:[ name:”pasta”, id:”2”, file:[id:”3”, nome:”file1.txt”, size:”100KB”]] sender:”<IP>”,receptor:”<IP>”}
                   //feedback: {"protocol":"pcmj", "status":200, "receptor":"200.196.154.1","command":"archive-list-back", "back":[{"id":3,"name":"valdirene"}, {"id":3,"name":"valdirene"}, {"id":3,"name":"valdirene"}], “sender”=”200.192.154.1”}
                    
                   //400
                   //status: 200 ok, 400,401,501
                   if(my_obj.getString("protocol").equals("pcmj") && my_obj.getString("sender").length() > 0 && my_obj.getString("receptor").length() > 0){
                       my_obj = new JSONObject();

                       try{
                            my_obj.put("protocol", "pcmj"); 
                            my_obj.put("command", "archive-list-back"); 
                            if(autenticado){
                                my_obj.put("status", "200"); 
                                File folder = new File("/home/regis/pcmj/");
                                File[] listOfFiles = folder.listFiles();
                                int i = 1;
                                JSONObject novo = new JSONObject();
                                for (File file : listOfFiles) {
                                    if (file.isFile()) {
                                        JSONObject aux = new JSONObject();
                                        aux.put("id", i);
                                        aux.put("nome", file.getName());
                                        aux.put("size",file.length());
                                        novo.put("file", aux);
                                        arquivos.add(i,"/home/regis/pcmj/"+ file.getName());
                                        //System.out.println(file.getName());
                                    }
                                    //my_obj.put("generos", my_genres);
                                    i++;
                                }
                                my_obj.put("back", novo);
                            }else if(arquivos.isEmpty()){
                                my_obj.put("status", "400"); // ???
                            }else if(!autenticado){
                                my_obj.put("status", "401"); 
                            }else{
                                my_obj.put("status", "501"); 
                            }
                            my_obj.put("sender", "localhost");
                            my_obj.put("receptor", "<IP>");           
                            outToClient.writeBytes(my_obj.toString()+"\n"); 
                       }catch(Exception erro){
                           //nao seria o caso de inserir aqui o cod 501 ?
                           System.out.println(erro.getMessage());
                       }
                   }else{
                       my_obj = new JSONObject();
                       my_obj.put("protocol", "pcmj"); 
                       my_obj.put("command", "archive-list-back"); 
                       my_obj.put("status", "400"); 
                       my_obj.put("sender", "localhost");
                       my_obj.put("receptor", "<IP>");
                       outToClient.writeBytes(my_obj.toString()+"\n");  
                   }
                   
               }else if(my_obj.getString("command").equals("archive-request")){ // {protocol:”pcmj”, command:”archive-request”, id:”<ID>” sender:”<IP>”,receptor:”<IP>”}
//ARRUMAR AQUI archive-request
                   //feedback: {protocol:”pcmj”, command:”archive-request-back”,status:”<CODIGO>”, id:”<ID>”, http_address:”<STRING>”, size:”200”, md5:”<STRING>”, sender:”<IP>”, receptor:”<IP>”}
                   //400,408,501
                   

                   
                   if(my_obj.getString("protocol").equals("pcmj") && my_obj.getString("sender").length() > 0 && my_obj.getString("receptor").length() > 0){
                       my_obj = new JSONObject();
                       my_obj.put("protocol", "pcmj"); 
                       my_obj.put("command", "archive-request-back");

                       if(!autenticado){
                          my_obj.put("status","401");
                       }else{
                           File folder = new File(arquivos.get(Integer.parseInt(my_obj.getString("id")))); 
                           if(folder.isFile()){
                              my_obj.put("status","302");
                           }else{
                              my_obj.put("status","404");
                           }
                       }    
                   }else{
                       my_obj = new JSONObject();
                       my_obj.put("protocol", "pcmj"); 
                       my_obj.put("command", "archive-request-back"); 
                       my_obj.put("status", "400"); 
                       my_obj.put("sender", "localhost");
                       my_obj.put("receptor", "<IP>");
                       outToClient.writeBytes(my_obj.toString()+"\n");  
                   }
                   
                }
                 
           }
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }  
   }
}
