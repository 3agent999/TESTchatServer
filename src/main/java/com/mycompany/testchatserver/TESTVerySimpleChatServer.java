/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.testchatserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class TESTVerySimpleChatServer {

    ArrayList clientOutputStreams;
//->начало внутреннего класса=========================----------->
    public class ClientHandler implements Runnable {

        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSOcket) {
            try {
                sock = clientSOcket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (IOException ex) {
            }
        }

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEveryone(message); //реализация метода отправки сообщения в jTextField клиента
                }
            } catch (IOException ex) {
            }
        }
    }//<-окончание внутреннего класса============================----------<

    
    
    public static void main(String[] args) {//<-main для запуска
        new TESTVerySimpleChatServer().go();
    }

    
    
    //->начало метода go Старт сервера ожидание подклчение, внутри находится ====THREAD====--->
    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(5000);
            while (true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));//====THREAD====
                t.start();
                System.out.println("got a connection");
            }
        } catch (IOException ex) {
        }
    } //<-окончание метода go----------------------------------------------------------------<

    
    
    //->начало метода -=сообщение, полученное от клиента пересылается в клиентский TextArea? тут или во внутреннем классе?--->
    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();//PrintWriter поподробнее--------------------------?
                writer.println(message);
                writer.flush();
            } catch (Exception ex) {
            }
        }
    } //<-окончание метода-------------------------------------------------------------------------------------------<
}
