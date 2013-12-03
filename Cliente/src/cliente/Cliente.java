/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
*
 */
package cliente;

/**
 *
 * @author regiszanandrea
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class Cliente {
    
   
   public static void main(String[] args) {
       Scanner sc = new Scanner(System.in);
       String comando;
       String modifiedcomando = "";
       
       JSONObject my_obj;
       while(true){
        try {
            
             // Cria um buffer que armazenará as informações de entrada do teclado
             BufferedReader inFromUSer = new BufferedReader(new InputStreamReader(System.in));

             // Cria um Socket cliente passando como parâmetro o ip e a porta do servidor   
             Socket client = new Socket("localhost", 9876);

             // Cria um stream de saída 
             DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());

             // Cria um buffer que armazenará as informações retornadas pelo servidor
             BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
             
             
              my_obj = new JSONObject(); // Instancia o Objeto Json
             // Atribui as informações armazenadas no buffer do teclado à variável "comando"
             comando = sc.nextLine();
            // System.out.println(comando.equals("ping"));
             switch(comando){
                 case "ping":
                    //{protocol:”pcmj”, command:”ping”, sender:”<IP>”,receptor:”<IP>”}
                    my_obj.put("protocol", "pcmj"); 
                    my_obj.put("command", "ping"); 
                    my_obj.put("sender", "<IP>");
                    my_obj.put("receptor", "localhost");
                    break;
                 case "authenticate":
                    //{protocol:”pcmj”, command:”authenticate”, passport:”<PASSPORT>, ”sender:”<IP>”, receptor:”<IP>”}
                    my_obj.put("protocol", "pcmj"); 
                    my_obj.put("command", "authenticate");
                    my_obj.put("passport", "DiJqWHqKtiDgZySAv7ZX");
                    my_obj.put("sender", "<IP>");
                    my_obj.put("sender", "localhost");
                    break;
                 case "agent-list":
                    //{protocol:”pcmj”, command:”agent-list”, sender:”<IP>”, receptor:”<IP>”}
                    my_obj.put("protocol", "pcmj"); 
                    my_obj.put("command", "agent-list");
                    my_obj.put("sender", "<IP>");
                    my_obj.put("sender", "localhost");
                    break;
                 case "archive-list":
                    //{protocol:”pcmj”, command:”archive-list”, sender:”<IP>”,receptor:”<IP>”}
                    break;
                 default:
                    System.out.println("Comando inválido");
                    break;
             }
         
             // Disponibiliza as informações contidas em "comando" para a stream de saída do cliente
             outToServer.writeBytes(my_obj.toString()+"\n");

             // Atribui as informações modificadas pelo servidor na variável "modifiedcomando"
             modifiedcomando = inFromServer.readLine();

             if(modifiedcomando.indexOf("status") != -1){
                 // Imprime no console do cliente a informação retornada pelo servidor
                System.out.println(new JSONObject(modifiedcomando).getString("status")); 
             }else if(modifiedcomando.indexOf("command") != -1){
                 // Imprime no console do cliente a informação retornada pelo servidor
                 System.out.println(new JSONObject(modifiedcomando).getString("command")); 
             }             
             // Fecha o Socket
             client.close();   

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       }
   } 
}
