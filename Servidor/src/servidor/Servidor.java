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

public class Servidor {

   public static void main(String[] args) {

       String clienteComando,capitalized = "";
    
       ArrayList<String> listaPeers = new ArrayList<String>();
       //listaPeers.add("");
       
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
               if(my_obj.getString("command").equals("ping")){
                    //{protocol:”pcmj”, command:”pong”,status:”<CODIGO>”,  sender:”<IP>”,receptor:”<IP>”}
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
                      
               }else if(my_obj.getString("command").equals("authenticate")){
                   //{protocol:”pcmj”, command:”authenticate-back”,status:”<CODIGO>”,”sender:”<IP>”,receptor:”<IP>”}
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
               }
                // Isso é o servidor principal que fará isso                     

               else if(my_obj.getString("command").equals("agent-list")){
                   //{protocol:”pcmj”, command:”agent-list”, sender:”<IP>”, receptor:”<IP>”}
                   //InetAddress IP=InetAddress.getLocalHost();
                   //System.out.println("IP of my system is := "+IP.getHostAddress());
               }else if(my_obj.getString("command").equals("archive-list")){
                   //{protocol:”pcmj”, command:”archive-list-back”, status:”<CODIGO>”, back:”file:[id:”1”, nome:”file.txt”, size:”200”], folder:[ name:”pasta”, id:”2”, file:[id:”3”, nome:”file1.txt”, size:”100KB”]] sender:”<IP>”,receptor:”<IP>”}
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
                                    //System.out.println(file.getName());
                                }
                                //my_obj.put("generos", my_genres);
                                i++;
                            }
                            my_obj.put("back", novo);
                        }else if(false){
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
                       System.out.println(erro.getMessage());
                   }
               }else{
                    my_obj.put("status", "400"); 

                }
           }
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }  
   }
}
